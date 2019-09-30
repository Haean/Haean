package player;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Music extends JFrame {
	static int m_Count = 0;
	static ArrayList<String> FilePath = new ArrayList<String>();

	Container c = getContentPane();
	static int a = 0;
	JLabel al = new JLabel();
	JButton open = new JButton("open");
	JButton play = new JButton("play");
	JButton stop = new JButton("stop");
	static JButton next = new JButton("next");
	JButton prev = new JButton("prev");
	JButton btn_repeat = new JButton("반복");
	JButton rand = new JButton("rand");
	JButton resume = new JButton("resume");
	JButton pause = new JButton("pause");
	String filePath = "";

	JPanel list = new JPanel(new GridLayout());
	DefaultListModel listModel = new DefaultListModel();

	static FileInputStream fis;

	static int pp = 0;
	static int x = 0;
	static boolean r = true; //무한반복
	int hit = 0;
	int repeat = 0;

	Music() {
		setTitle("테스트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout());

		
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileOpen();
			}

		});
		c.add(al);
		
		c.add(open);
	
		c.add(play);
		c.add(stop);
		c.add(next);
		c.add(prev);
		c.add(rand);
		//c.add(btn_repeat);
		c.add(resume);
		c.add(pause);
		
		play.addMouseListener(new buttonclick1());
		stop.addMouseListener(new buttonclick2());
		next.addMouseListener(new buttonclick3());
		rand.addMouseListener(new buttonclick4());
		prev.addMouseListener(new buttonclick5());
		pause.addMouseListener(new buttonclick6());
		resume.addMouseListener(new buttonclick7());
		

		list.setSize(500, 500);
		c.add(list);

		JList Tlist = new JList(listModel);
		list.add(new JScrollPane(Tlist));

		setSize(500, 500);
		setVisible(true);
	}

	public void fileOpen() {
		JFileChooser chooser = new JFileChooser(); // 객체 생성
		int ret = chooser.showOpenDialog(null); // 열기창 정의

		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "경로를 선택하지않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			return;
		}

		filePath = chooser.getSelectedFile().getPath();// 파일경로를 가져옴
		FilePath.add(filePath);
		al.setText(filePath);

		listModel.addElement(filePath);
		JList list1 = new JList(listModel);

	}

	public static void main(String[] args) {
		new Music();
	}

	class buttonclick1 implements MouseListener {
		BBB ha = new BBB();
		Db path = new Db();
		
		@Override
		public void mouseClicked(MouseEvent e) {
			ha.Start();	

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
	//stop
	class buttonclick2 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			ha.stop();
		}
	}
	//next
	class buttonclick3 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			a = path.Pre() + 1;
			ha.stop();
			ha.Open(path.load(a));
			ha.stateCode = ha.STATE_INIT;
			try {
				ha.start(path.load(a));
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	class buttonclick4 extends buttonclick1 implements MouseListener {
		int r = 0;

		public void mouseClicked(MouseEvent e) {
			r = path.Random();
			ha.stop();
			ha.Open(path.load(r));
			ha.stateCode = ha.STATE_INIT;
			try {
				ha.start(path.load(r));
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	//prev
	class buttonclick5 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			a = path.Pre() - 1;
			ha.stop();
			ha.Open(path.load(a));
			ha.stateCode = ha.STATE_INIT;
			try {
				ha.start(path.load(a));
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	//pause
	class buttonclick6 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			ha.pause();
		}
	}

	class buttonclick7 extends buttonclick1 implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			ha.resume();
		}
	}

}