package player;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class MusicPlayer extends JFrame {

	static int a = 0;
	int pause = 0;
	static int repeat = 0;
	String filePath = "";
	DefaultListModel listModel = new DefaultListModel();
	static ArrayList<String> FilePath = new ArrayList<String>();

	static FloatControl control;

	//object 생성
	
	JFrame frm_home = new JFrame(); // 홈 화면
	JFrame frm_main = new JFrame(); // 메인 화면
	JPanel p_home = new JPanel(); // 홈 화면 패널
	JPanel play = new JPanel();// 메인 안 재생 패널
	JLabel mainlabel = new JLabel("Play List"); // playlist 라벨 생성
	JLabel chart = new JLabel("실시간 차트"); // 실시간 차트 라벨 생성
	JLabel title = new JLabel("기억해줘요 내 모든 날과  그때를"); // 홈 화면 - 노래 제목 라벨 생성
	JLabel singer = new JLabel("거미"); // 메인 - 가수 라벨 생성
	JLabel lyrics = new JLabel(); // 메인 - 노래 가사 라벨 생성

	JLabel homeTimelab2 = new JLabel("시 기준");
	JLabel homeTimelab = new JLabel(); // 홈 화면 - 시간 라벨 생성
	JLabel name = new JLabel(); // 홈 화면 노래 제목, 가수 라벨 생성
	JLabel homeVolLab = new JLabel(); // 홈 화면 = 볼륨 수치 라벨
	JLabel mainVolLab = new JLabel(); // 메인 화면 = 볼륨 수치 라벨

	ImageIcon volumeIcon = new ImageIcon("image/volume.png"); // 음량
	ImageIcon qksqhr = new ImageIcon("image/qksqhr.png"); // 전체 반복
	ImageIcon qksqhr1 = new ImageIcon("image/1qksqhr.png"); // 한 곡 반복
	ImageIcon shuffle = new ImageIcon("image/shuffle.png"); // 셔플(랜덤)
	ImageIcon image = new ImageIcon("image/image.jpg"); // 앨범 사진
	ImageIcon bar = new ImageIcon("image/bar.jpg"); // 재생 바
	ImageIcon exit = new ImageIcon("image/exit.png"); // 나가기
	ImageIcon list = new ImageIcon("image/list.png"); // 리스트
	ImageIcon clock = new ImageIcon("image/clock.png"); // 시계 이미지

//  static final int N_STATE_ICON = 1;
//  static final int F_STATE_ICON = 0;
//  int btn_state =0;

	JLabel homeTimeImalab = new JLabel(clock);// 홈 - 시간 기준 이미지
	JButton mainPlayBtn = new JButton("▶");// 메인 화면 - 재생
	JButton mainStopBtn = new JButton("■");// 메인 화면 - 정지
	JButton mainPreBtn = new JButton("◀◀"); // 메인 화면 - 이전 곡
	JButton mainNextBtn = new JButton("▶▶"); // 메인 화면 - 다음 곡
	JButton shuffleButton = new JButton(shuffle); // 메인 화면 - 셔플
	JButton repetition = new JButton(qksqhr); // 메인 화면 - 전체 반복
	JButton volumeButton = new JButton(volumeIcon); // 메인 화면 - 음량
	JButton imageButton = new JButton(image); // 메인 화면 - 앨범 이미지
	JButton barButton = new JButton(bar); // 메인 화면 - 재생 바
	JButton exitButton = new JButton(exit); // 메인 화면 - 나가기

	JButton homeListBtn = new JButton(list); // 홈 화면 - 재생목록 버튼
	JButton homeVolBtn = new JButton(volumeIcon); // 홈 화면 - 음량
	JButton homePlayBtn = new JButton("▶"); // 홈 화면 - 재생
	JButton homePreBtn = new JButton("◀◀"); // 홈 화면 - 이전 곡
	JButton homeNextBtn = new JButton("▶▶"); // 홈 화면 - 다음 곡

	JSlider homesl = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // 홈 화면 - 음량 조절 슬라이더
	JSlider mainsl = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // 메인 - 음량 조절 슬라이더

	//J프레임
	MusicPlayer() {
		frm_home.setTitle("HOME"); // 홈 화면 프레임
		frm_main.setTitle("Music Player"); // 메인 화면 프레임
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		play.setBackground(Color.LIGHT_GRAY); // 메인 화면 배경 색 지정
		p_home.setBackground(Color.LIGHT_GRAY); // 홈 화면 배경 색 지정

		setLayout(new BorderLayout());
		play.setLayout(null); // main 안 패널
		p_home.setLayout(null); // home 안 패널

		homeTime();
		homeList();

//      barButton.setBounds(0, 500, 600, 40);
//      barButton.setBackground(Color.white);
//      play.add(barButton);

		home_label(); // 함수 호출
		home_Btn();
		home_Btn2();
		homeVol();

		mainMenu(); // 메뉴 생성 함수
		mainList();
		main_Btn();
		mainPlayView();
		mainVol();

		frm_main.add(play);
		frm_main.setSize(600, 700);
		frm_main.setVisible(false);

		frm_home.add(p_home);
		frm_home.setSize(600, 700);
		frm_home.setVisible(true);

	}

	void home_Btn() { // 홈 재생관련 버튼 함수 생성

		homePreBtn.setBounds(360, 580, 70, 70);
		homePreBtn.setBackground(Color.LIGHT_GRAY);
		homePreBtn.setForeground(Color.BLACK);
		homePreBtn.setBorderPainted(false); // 버튼 테두리 없앰
		homePreBtn.setFont(new Font("Gothic", Font.ITALIC, 15));
		homePreBtn.addMouseListener(new buttonclick5());
		p_home.add(homePreBtn);

		homePlayBtn.setBounds(430, 580, 70, 70);
		homePlayBtn.setBackground(Color.LIGHT_GRAY);
		homePlayBtn.setForeground(Color.BLACK);
		homePlayBtn.setBorderPainted(false);
		homePlayBtn.setFont(new Font("Gothic", Font.ITALIC, 15));
		homePlayBtn.addMouseListener(new buttonclick1());
		p_home.add(homePlayBtn);

		homeNextBtn.setBounds(500, 580, 70, 70);
		homeNextBtn.setBackground(Color.LIGHT_GRAY);
		homeNextBtn.setForeground(Color.BLACK);
		homeNextBtn.setBorderPainted(false);
		homeNextBtn.setFont(new Font("Gothic", Font.ITALIC, 15));
		homeNextBtn.addMouseListener(new buttonclick3());
		p_home.add(homeNextBtn);

	}

	void home_Btn2() { // 홈 화면 - 목록 버튼 및 나가기 버튼
		homeListBtn.setBackground(Color.LIGHT_GRAY);
		homeListBtn.setForeground(Color.DARK_GRAY);
		homeListBtn.setFont(new Font("Gothic", Font.ITALIC, 20));
		homeListBtn.setBorderPainted(false);
		homeListBtn.setBounds(5, 580, 70, 70);
		homeListBtn.addActionListener(new MainActionListener());
		p_home.add(homeListBtn);

		exitButton.setBounds(535, 0, 50, 50); // 나가기 버튼
		exitButton.setBackground(Color.LIGHT_GRAY);
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new exitActionListener());
		p_home.add(exitButton);
	}

	void homeVol() { // 홈 음량 조절 버튼 함수 생성

		homeVolBtn.setBounds(490, 0, 50, 50);
		homeVolBtn.setBackground(Color.LIGHT_GRAY);
		homeVolBtn.setBorderPainted(false);
		homeVolBtn.addActionListener(new MyActionListener());
		p_home.add(homeVolBtn);

		homeVolLab.setBounds(280, 0, 200, 50);
		p_home.add(homeVolLab);

		homesl.setPaintLabels(true);
		homesl.setPaintTicks(true);
		homesl.setPaintTrack(true);
		homesl.setMajorTickSpacing(10);
		homesl.setMinorTickSpacing(5);
		homesl.setBackground(Color.LIGHT_GRAY);
		homesl.setBounds(330, 0, 200, 50);
		p_home.add(homesl);

		homesl.setVisible(false);
		homesl.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				homeVolLab.setText("" + homesl.getValue());
			}
		});

	}

	void homeTime() { // 홈 시간 함수 생성

		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm"); // 현재 시간
		Date time = new Date();
		String time1 = format1.format(time);
		homeTimelab.setText(time1);
		homeTimelab.setBackground(Color.LIGHT_GRAY);
		homeTimelab.setForeground(Color.DARK_GRAY);
		homeTimelab.setFont(new Font("Gothic", Font.BOLD, 20));
		homeTimelab.setBounds(10, 0, 100, 50);
		p_home.add(homeTimelab);

		homeTimeImalab.setBounds(128, 72, 15, 15);
		p_home.add(homeTimeImalab);

		SimpleDateFormat simpledate = new SimpleDateFormat("HH"); // 현재 시간
		Date date = new Date();
		String time2 = simpledate.format(date);
		homeTimelab2.setText(time2 + "시 기준");
		homeTimelab2.setBounds(150, 60, 70, 40);
		homeTimelab2.setBackground(Color.lightGray);
		homeTimelab2.setForeground(Color.BLACK);
		homeTimelab2.setFont(new Font("Gothic", Font.ITALIC, 13));
		p_home.add(homeTimelab2);

	}

	void homeList() { // 홈 리스트 함수 생성
		Db.list(FilePath);
		JList hList = new JList(FilePath.toArray());
		JScrollPane scrollpane1 = new JScrollPane(hList);
		scrollpane1.setBounds(5, 100, 580, 450);
		p_home.add(scrollpane1);

		chart.setBounds(10, 40, 200, 70);
		chart.setForeground(Color.DARK_GRAY);
		chart.setFont(new Font("Gothic", Font.ITALIC, 20));
		p_home.add(chart);
	}

	void home_label() { // 홈 화면 - 노래 제목 및 가수 이름
		name.setText("<html>" + "기억해줘요 내 모든 날과 그때를<br>거미" + "</html>");
		name.setForeground(Color.black);
		name.setBackground(Color.LIGHT_GRAY);
		name.setFont(new Font("Gothic", Font.ITALIC, 15));
		name.setBounds(80, 593, 300, 50);
		p_home.add(name);
	}

	void mainList() { // 메인 리스트 함수 생성
		Db.list(FilePath);
		JList mainList = new JList(FilePath.toArray());
		JScrollPane scrollpane = new JScrollPane(mainList);
		scrollpane.setBounds(0, 50, 587, 200);
		play.add(scrollpane);

		mainlabel.setFont(new Font("Gothic", Font.ITALIC, 20));
		mainlabel.setBounds(5, 0, 600, 50);
		play.add(mainlabel);
	}

	void main_Btn() { // 메인 재생관련 버튼 함수 생성
		repetition.setBackground(Color.LIGHT_GRAY);
		repetition.setBounds(0, 540, 100, 100);
		repetition.addMouseListener(new buttonclick6());
		play.add(repetition);

		mainPreBtn.setBackground(Color.LIGHT_GRAY);
		mainPreBtn.setForeground(Color.BLACK);
		mainPreBtn.setFont(new Font("Gothic", Font.ITALIC, 20));
		mainPreBtn.setBounds(100, 540, 100, 100);
		mainPreBtn.addMouseListener(new buttonclick5());
		play.add(mainPreBtn);

		mainPlayBtn.setBackground(Color.LIGHT_GRAY);
		mainPlayBtn.setForeground(Color.BLACK);
		mainPlayBtn.setFont(new Font("Gothic", Font.ITALIC, 20));
		mainPlayBtn.setBounds(200, 540, 100, 100);
		mainPlayBtn.addMouseListener(new buttonclick1());
		play.add(mainPlayBtn);

		mainStopBtn.setBackground(Color.LIGHT_GRAY);
		mainStopBtn.setBounds(300, 540, 100, 100);
		mainStopBtn.addMouseListener(new buttonclick2());
		play.add(mainStopBtn);

		mainNextBtn.setBackground(Color.LIGHT_GRAY);
		mainNextBtn.setForeground(Color.BLACK);
		mainNextBtn.setFont(new Font("Gothic", Font.ITALIC, 20));
		mainNextBtn.setBounds(400, 540, 100, 100);
		mainNextBtn.addMouseListener(new buttonclick3());
		play.add(mainNextBtn);

		shuffleButton.setBackground(Color.LIGHT_GRAY);
		shuffleButton.setBounds(500, 540, 100, 100);
		play.add(shuffleButton);
	}

	void mainPlayView() { // 메인 앨범이미지 및 가사 함수 생성
		imageButton.setBounds(10, 300, 200, 200);
		play.add(imageButton);

		singer.setFont(new Font("Gothic", Font.BOLD, 20));
		singer.setBounds(80, 220, 50, 90);
		play.add(singer);

		title.setFont(new Font("Gothic", Font.BOLD, 13));
		title.setBounds(15, 250, 300, 70);
		play.add(title);

		lyrics.setText("<html>"
				+ "듣고 있나요 나의 이 모든 얘기를 <br> 그댈 향한 내 깊은 진심을 매일 그리움 속에 <br>그대를 불러보지만 <br>닿을 수 없는 마음을 <br>나도 이젠 알 것 같아요<br>내 안의 그대를 놓을 수 없네요<br>애써도 그게 잘 안돼요<br>마음과 반대로 밀어내려 할수록<br> 이토록 더 아파지네요<br>기억하나요 나의 이 모든 얘기를<br>그댈 향한 내 깊은 진심을<br>매일 그리움 속에 그대를 불러보지만 <br>닿을 수 없는 마을을 나도 이젠 알 것 같아요<br>"
				+ "</html>");
		lyrics.setFont(new Font("Gothic", Font.LAYOUT_LEFT_TO_RIGHT, 12));
		lyrics.setBorder(new TitledBorder(new LineBorder(Color.black, 1)));
		lyrics.setBounds(250, 260, 300, 240);
		play.add(lyrics);
	}

	void mainVol() { // 메인 음량 조절 버튼 함수 생성
		volumeButton.setBounds(535, 0, 50, 50);
		volumeButton.setBackground(Color.LIGHT_GRAY);
		volumeButton.setBorderPainted(false);
		volumeButton.addActionListener(new MyActionListener());
		play.add(volumeButton);
		mainsl.setVisible(false);

		mainVolLab.setBounds(330, 0, 200, 50);
		play.add(mainVolLab);

		mainsl.setPaintLabels(true);
		mainsl.setPaintTicks(true);
		mainsl.setPaintTrack(true);
		mainsl.setMajorTickSpacing(10);
		mainsl.setMinorTickSpacing(5);
		mainsl.setBackground(Color.LIGHT_GRAY);
		mainsl.setBounds(380, 0, 200, 50);
		play.add(mainsl);

		mainsl.addChangeListener(new VolListener());
	}

	void mainMenu() { // 메인 메뉴 버튼 함수 생성

		JMenuBar menubar = new JMenuBar(); // 메뉴바 생성
		JMenu Mfile = new JMenu("file"); // 첫번째 메뉴
		JMenu Mhelp = new JMenu("option"); // 두번째 메뉴

		JMenuItem add = new JMenuItem("add");
		Mfile.add(add); // 첫번째 메뉴의 1속성
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fileOpen();
				homeList();
			}
		});
		Mfile.add(new JMenuItem("delete")); // 첫번째 메뉴의 2속성

		JMenuItem back = new JMenuItem("back");
		JMenuItem exit = new JMenuItem("exit");
		Mhelp.add(back); // 두번째 메뉴의 1속성
		Mhelp.add(exit);// 두번째 메뉴의 2 속성
		back.addActionListener(new backActionListener());
		exit.addActionListener(new MenuActionListener());

		menubar.add(Mfile); // 메뉴바에 옵션추가
		menubar.add(Mhelp);
		frm_main.setJMenuBar(menubar);
	}

	class exitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JLabel text = new JLabel();
			int result = JOptionPane.showConfirmDialog(null, "정말로 나가시겠습니까?", "EXIT", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.CLOSED_OPTION) {
				text.setText("Closed");
			} else if (result == JOptionPane.YES_OPTION) {
				text.setText("YES");
				System.exit(0);
			} else {
				text.setText("NO");
			}

		}

	}

	class MainActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frm_main.setVisible(true);
			frm_home.setVisible(false);
		}
	}

	class backActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frm_main.setVisible(false);
			frm_home.setVisible(true);
		}

	}

	class MenuActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JLabel text = new JLabel();
			int result = JOptionPane.showConfirmDialog(null, "정말로 나가시겠습니까?", "EXIT", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.CLOSED_OPTION) {
				text.setText("Closed");
			} else if (result == JOptionPane.YES_OPTION) {
				text.setText("YES");
				System.exit(0);
			} else {
				text.setText("NO");
			}
		}
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			TimerThread th = new TimerThread(btn);
			th.start();
		}
	}

	class TimerThread extends Thread {
		JButton btn;

		public TimerThread(JButton btn) {
			this.btn = btn;
		}

		@Override
		public void run() {
			try {
				btn.setVisible(false);
				mainsl.setVisible(true);
				homesl.setVisible(true);
				homeVolLab.setVisible(true);
				mainVolLab.setVisible(true);

				Thread.sleep(3000);
				btn.setVisible(true);
				mainsl.setVisible(false);
				homesl.setVisible(false);
				homeVolLab.setVisible(false);
				mainVolLab.setVisible(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	
	class buttonclick1 implements MouseListener {
		BBB ha = new BBB();
		Db path = new Db();

		@Override
		public void mouseClicked(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			if (btn.getText().equals("▶")) {
				if (pause == 1) {
					ha.resume();
				} else {
					ha.Start();
				}
				btn.setText("||");

			} else {
				ha.pause();
				pause = 1;
				btn.setText("▶");
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

	}

	// stop
	class buttonclick2 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			ha.stop();
			pause = 0;
			mainPlayBtn.setText("▶");
		}
	}

	// next
	class buttonclick3 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			a = path.Pre() + 1;
			ha.stop();
			ha.Open(Db.load(a));
			BBB.stateCode = BBB.STATE_INIT;
			try {
				ha.start(Db.load(a));
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	// random
//	class buttonclick4 extends buttonclick1 implements MouseListener {
//		int r = 0;
//
//		public void mouseClicked(MouseEvent e) {
//			r = path.Random();
//			ha.stop();
//			ha.Open(path.load(r));
//			ha.stateCode = ha.STATE_INIT;
//			try {
//				ha.start(path.load(r));
//			} catch (UnsupportedAudioFileException e1) {
//				e1.printStackTrace();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//
//		}
//	}

	class buttonclick5 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			a = path.Pre() - 1;
			ha.stop();
			ha.Open(Db.load(a));
			BBB.stateCode = BBB.STATE_INIT;
			try {
				ha.start(Db.load(a));
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class buttonclick6 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			JButton btn = (JButton) e.getSource();

			if (btn.getIcon().equals(qksqhr)) {
				btn.setIcon(qksqhr1);
				repeat = 1;

			} else {
				btn.setIcon(qksqhr);
				repeat = 0;
			}
		}
	}
	
	
	//볼륨조절
	class VolListener extends buttonclick1 implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			int a = mainsl.getValue();
			float b = (a/100f);
			control.setValue(b);
			System.out.println("control 값 : "+control.getValue());
		}
	}

	public void fileOpen() {
		JFileChooser chooser = new JFileChooser(); // 객체 생성
		int ret = chooser.showOpenDialog(null); // 열기창 정의

		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "경로를 선택하지않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

		filePath = chooser.getSelectedFile().getPath();// 파일경로를 가져옴
	    Db.insert(filePath);
	    Db.list(FilePath);
	    
	    mainList();
	}
	
	public static void volumecontrol() {
		javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();

		Mixer.Info mixerInfo = mixers[0];
		System.out.println(mixerInfo);
		Mixer mixer = AudioSystem.getMixer(mixerInfo);
		System.out.println(mixer);
		Line.Info[] lineinfos = mixer.getTargetLineInfo();
		System.out.println(lineinfos);

		try {
			System.out.println("try");
			Line line = mixer.getLine(lineinfos[0]);
			System.out.println(line);
			line.open();
			if (line.isControlSupported(FloatControl.Type.VOLUME)) {
				control = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
				control.setValue((float) 0.5);
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		volumecontrol();
		new MusicPlayer();
		
	}
}