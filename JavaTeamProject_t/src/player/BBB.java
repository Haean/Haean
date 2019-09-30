package player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class BBB {
	//버퍼를 설정
	static class Sample {
		private short[] buffer;
		private int length;

		public Sample(short[] buf, int s) {
			buffer = buf.clone();
			length = s;
		}

		public short[] GetBuffer() {
			return buffer;
		}

		public int GetLength() {
			return length;
		}
	}

	static int Stop = 0;
	int x=0;
	
	Db path = new Db();
	int index = 0;
	int y = 0;
	
	String name = "";
	
	public static final int BUFFER_SIZE = 10000;

	private Decoder decoder;
	private AudioDevice out;
	private ArrayList<Sample> playes;
	private int length;

	public boolean IsInvalid() {
		return (decoder == null || out == null || playes == null || !out.isOpen());
	}
	
	protected boolean Getplayes(String path) {
		if (IsInvalid())
			return false;
		try {
			Header header;
			SampleBuffer pb;
			FileInputStream in = new FileInputStream(path);
			Bitstream bitstream = new Bitstream(in);
			if ((header = bitstream.readFrame()) == null)
				return false;
			while (length < BUFFER_SIZE && header != null) {
				pb = (SampleBuffer) decoder.decodeFrame(header, bitstream);
				playes.add(new Sample(pb.getBuffer(), pb.getBufferLength()));
				length++;
				bitstream.closeFrame();
				header = bitstream.readFrame();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 매개변수를 이용해 파일 열기
	public boolean Open(String path) {
		try {
			decoder = new Decoder();
			out = FactoryRegistry.systemRegistry().createAudioDevice();
			playes = new ArrayList<Sample>(BUFFER_SIZE);
			length = 0;
			out.open(decoder);
			Getplayes(path);
		} catch (Exception e) {
			decoder = null;
			out = null;
			playes = null;
			return false;
		}
		return true;
	}

	private ThreadB thisThread;
	private ThreadA AA;

	final static int STATE_INIT = 0;
	final static int STATE_STARTED = 1;
	final static int STATE_PAUSED = 2;
	final static int STATE_STOPPED = 3;

	static int stateCode = STATE_INIT;

//	public void start() {
//		synchronized (this) {
//			thisThread = new ThreadB();
//			AA = new ThreadA();
//
//			AA.start();
//			thisThread.start();
//
//			stateCode = STATE_STARTED;
//		}
//	}
	//노래 시작
	public void start(String path) throws UnsupportedAudioFileException, IOException {
		synchronized (this) {
			x = f_l(path);
			thisThread = new ThreadB();
			AA = new ThreadA();

			AA.start();
			thisThread.start();
			stateCode = STATE_STARTED;
		}
	}

	public void stop() {
		synchronized (this) {
			BBB.stateCode = STATE_STOPPED;
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("정지");
		}
	}
	//노래 재생에 대한 코드
	public void play() {
		if (IsInvalid()) {
			return;
		}
		System.out.println("db실행중");
		try {
			
			for (int i = 0; i < length; i++) {
				out.write(playes.get(i).GetBuffer(), 0, playes.get(i).GetLength());
				if (stateCode == STATE_STOPPED) {
					Close();
					AA.stop();
				}
				if (stateCode == STATE_PAUSED) {
					while (true) {
						try {
							//thisThread.sleep(100);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (stateCode == STATE_STARTED || stateCode == STATE_STOPPED) {
							break;
						}
					}
				}
			}
		} catch (JavaLayerException e) {
			Close();
			AA.stop();
		}
	}
	//노래정지 됬을때 초기화
	public void Close() {
		if ((out != null) && !out.isOpen()) {
			out.close();
		}
		length = 0;
		playes = null;
		out = null;
		decoder = null;
	}
	//일시정지
	public void pause() {
		if (stateCode == STATE_PAUSED) {
			return;
		} else if (stateCode == STATE_INIT) {
			System.out.println("실행중 X");
		} else if (stateCode == STATE_STOPPED) {
			System.out.println("정지상태");
		} else {
			System.out.println("일시정지");
			stateCode = STATE_PAUSED;
		}
	}
	//다시 시작
	public void resume() {
		if (stateCode == STATE_STARTED || stateCode == STATE_INIT) {
			System.out.println("실행중 X");
			return;
		}
		if (stateCode == STATE_STOPPED) {
			System.out.println("정지상태");
		}
		stateCode = STATE_STARTED;
		System.out.println("되돌리기");
	}
	
	//여기서부터 스피커
	static LinkedList<Line> speakers = new LinkedList<Line>();

	final static void findSpeakers() {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();

		for (Mixer.Info mixerInfo : mixers) {
			if (!mixerInfo.getName().contentEquals("Java Sound Audio Engine"))
				continue;

			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lines = mixer.getSourceLineInfo();

			for (Line.Info info : lines) {
				try {
					Line line = mixer.getLine(info);
					speakers.add(line);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException iaEx) {}
			}
		}
	}

	static {
		findSpeakers();
	}

	public static void setVolume(float level) {
		System.out.println("볼륨 : " + level);
		for (Line line : speakers) {
			try {
				line.open();
				FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
				control.setValue(limit(control, level));
			} catch (LineUnavailableException e) {
				continue;
			} catch (java.lang.IllegalArgumentException e) {
				continue;
			}
		}

	}

	private static float limit(FloatControl control, float level) {
		return Math.min(control.getMaximum(), Math.max(control.getMinimum(), level));
	}
	//파일 재생길이 구하기
	public static int f_l(String path) throws UnsupportedAudioFileException, IOException {

		File file = new File(path);
		AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
		if (fileFormat instanceof TAudioFileFormat) {
			Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
			String key = "duration";
			Long microseconds = (Long) properties.get(key);
			int mili = (int) (microseconds / 1000);
			int sec = (mili / 1000);
			return sec;
		} else {
			throw new UnsupportedAudioFileException();
		}

	}
	//현재 재생시간
	class ThreadA extends Thread {
		public void run() {
			int n = 0;
			while (true) {
				if(stateCode == STATE_PAUSED) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					n++;
					try {
						sleep(1000);
						System.out.println(n);
						if (n == x) {
							thisThread.stop();
							if(MusicPlayer.repeat == 0) {
								index++;
							}
							Start();
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}
	}
	// 노래하는 쓰레드
	class ThreadB extends Thread{
		public void run() {
			System.out.println("노래시작");
			while (true) {
				if (stateCode == STATE_STOPPED) {
					break;
				}
				play();
			}
		}
	}
	// 자동으로 넘어가게 하려고 함수 만듬
	public void Start() {
		
		stop();
		Open(Db.load(index));
		index = path.Pre();//index값
		name = path.Name();//파일명
		stateCode = STATE_INIT;
		try {
			start(Db.load(index));
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	

}