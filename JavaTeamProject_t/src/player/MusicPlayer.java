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

	//object ����
	
	JFrame frm_home = new JFrame(); // Ȩ ȭ��
	JFrame frm_main = new JFrame(); // ���� ȭ��
	JPanel p_home = new JPanel(); // Ȩ ȭ�� �г�
	JPanel play = new JPanel();// ���� �� ��� �г�
	JLabel mainlabel = new JLabel("Play List"); // playlist �� ����
	JLabel chart = new JLabel("�ǽð� ��Ʈ"); // �ǽð� ��Ʈ �� ����
	JLabel title = new JLabel("�������� �� ��� ����  �׶���"); // Ȩ ȭ�� - �뷡 ���� �� ����
	JLabel singer = new JLabel("�Ź�"); // ���� - ���� �� ����
	JLabel lyrics = new JLabel(); // ���� - �뷡 ���� �� ����

	JLabel homeTimelab2 = new JLabel("�� ����");
	JLabel homeTimelab = new JLabel(); // Ȩ ȭ�� - �ð� �� ����
	JLabel name = new JLabel(); // Ȩ ȭ�� �뷡 ����, ���� �� ����
	JLabel homeVolLab = new JLabel(); // Ȩ ȭ�� = ���� ��ġ ��
	JLabel mainVolLab = new JLabel(); // ���� ȭ�� = ���� ��ġ ��

	ImageIcon volumeIcon = new ImageIcon("image/volume.png"); // ����
	ImageIcon qksqhr = new ImageIcon("image/qksqhr.png"); // ��ü �ݺ�
	ImageIcon qksqhr1 = new ImageIcon("image/1qksqhr.png"); // �� �� �ݺ�
	ImageIcon shuffle = new ImageIcon("image/shuffle.png"); // ����(����)
	ImageIcon image = new ImageIcon("image/image.jpg"); // �ٹ� ����
	ImageIcon bar = new ImageIcon("image/bar.jpg"); // ��� ��
	ImageIcon exit = new ImageIcon("image/exit.png"); // ������
	ImageIcon list = new ImageIcon("image/list.png"); // ����Ʈ
	ImageIcon clock = new ImageIcon("image/clock.png"); // �ð� �̹���

//  static final int N_STATE_ICON = 1;
//  static final int F_STATE_ICON = 0;
//  int btn_state =0;

	JLabel homeTimeImalab = new JLabel(clock);// Ȩ - �ð� ���� �̹���
	JButton mainPlayBtn = new JButton("��");// ���� ȭ�� - ���
	JButton mainStopBtn = new JButton("��");// ���� ȭ�� - ����
	JButton mainPreBtn = new JButton("����"); // ���� ȭ�� - ���� ��
	JButton mainNextBtn = new JButton("����"); // ���� ȭ�� - ���� ��
	JButton shuffleButton = new JButton(shuffle); // ���� ȭ�� - ����
	JButton repetition = new JButton(qksqhr); // ���� ȭ�� - ��ü �ݺ�
	JButton volumeButton = new JButton(volumeIcon); // ���� ȭ�� - ����
	JButton imageButton = new JButton(image); // ���� ȭ�� - �ٹ� �̹���
	JButton barButton = new JButton(bar); // ���� ȭ�� - ��� ��
	JButton exitButton = new JButton(exit); // ���� ȭ�� - ������

	JButton homeListBtn = new JButton(list); // Ȩ ȭ�� - ������ ��ư
	JButton homeVolBtn = new JButton(volumeIcon); // Ȩ ȭ�� - ����
	JButton homePlayBtn = new JButton("��"); // Ȩ ȭ�� - ���
	JButton homePreBtn = new JButton("����"); // Ȩ ȭ�� - ���� ��
	JButton homeNextBtn = new JButton("����"); // Ȩ ȭ�� - ���� ��

	JSlider homesl = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // Ȩ ȭ�� - ���� ���� �����̴�
	JSlider mainsl = new JSlider(JSlider.HORIZONTAL, 0, 100, 50); // ���� - ���� ���� �����̴�

	//J������
	MusicPlayer() {
		frm_home.setTitle("HOME"); // Ȩ ȭ�� ������
		frm_main.setTitle("Music Player"); // ���� ȭ�� ������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		play.setBackground(Color.LIGHT_GRAY); // ���� ȭ�� ��� �� ����
		p_home.setBackground(Color.LIGHT_GRAY); // Ȩ ȭ�� ��� �� ����

		setLayout(new BorderLayout());
		play.setLayout(null); // main �� �г�
		p_home.setLayout(null); // home �� �г�

		homeTime();
		homeList();

//      barButton.setBounds(0, 500, 600, 40);
//      barButton.setBackground(Color.white);
//      play.add(barButton);

		home_label(); // �Լ� ȣ��
		home_Btn();
		home_Btn2();
		homeVol();

		mainMenu(); // �޴� ���� �Լ�
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

	void home_Btn() { // Ȩ ������� ��ư �Լ� ����

		homePreBtn.setBounds(360, 580, 70, 70);
		homePreBtn.setBackground(Color.LIGHT_GRAY);
		homePreBtn.setForeground(Color.BLACK);
		homePreBtn.setBorderPainted(false); // ��ư �׵θ� ����
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

	void home_Btn2() { // Ȩ ȭ�� - ��� ��ư �� ������ ��ư
		homeListBtn.setBackground(Color.LIGHT_GRAY);
		homeListBtn.setForeground(Color.DARK_GRAY);
		homeListBtn.setFont(new Font("Gothic", Font.ITALIC, 20));
		homeListBtn.setBorderPainted(false);
		homeListBtn.setBounds(5, 580, 70, 70);
		homeListBtn.addActionListener(new MainActionListener());
		p_home.add(homeListBtn);

		exitButton.setBounds(535, 0, 50, 50); // ������ ��ư
		exitButton.setBackground(Color.LIGHT_GRAY);
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new exitActionListener());
		p_home.add(exitButton);
	}

	void homeVol() { // Ȩ ���� ���� ��ư �Լ� ����

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

	void homeTime() { // Ȩ �ð� �Լ� ����

		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm"); // ���� �ð�
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

		SimpleDateFormat simpledate = new SimpleDateFormat("HH"); // ���� �ð�
		Date date = new Date();
		String time2 = simpledate.format(date);
		homeTimelab2.setText(time2 + "�� ����");
		homeTimelab2.setBounds(150, 60, 70, 40);
		homeTimelab2.setBackground(Color.lightGray);
		homeTimelab2.setForeground(Color.BLACK);
		homeTimelab2.setFont(new Font("Gothic", Font.ITALIC, 13));
		p_home.add(homeTimelab2);

	}

	void homeList() { // Ȩ ����Ʈ �Լ� ����
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

	void home_label() { // Ȩ ȭ�� - �뷡 ���� �� ���� �̸�
		name.setText("<html>" + "�������� �� ��� ���� �׶���<br>�Ź�" + "</html>");
		name.setForeground(Color.black);
		name.setBackground(Color.LIGHT_GRAY);
		name.setFont(new Font("Gothic", Font.ITALIC, 15));
		name.setBounds(80, 593, 300, 50);
		p_home.add(name);
	}

	void mainList() { // ���� ����Ʈ �Լ� ����
		Db.list(FilePath);
		JList mainList = new JList(FilePath.toArray());
		JScrollPane scrollpane = new JScrollPane(mainList);
		scrollpane.setBounds(0, 50, 587, 200);
		play.add(scrollpane);

		mainlabel.setFont(new Font("Gothic", Font.ITALIC, 20));
		mainlabel.setBounds(5, 0, 600, 50);
		play.add(mainlabel);
	}

	void main_Btn() { // ���� ������� ��ư �Լ� ����
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

	void mainPlayView() { // ���� �ٹ��̹��� �� ���� �Լ� ����
		imageButton.setBounds(10, 300, 200, 200);
		play.add(imageButton);

		singer.setFont(new Font("Gothic", Font.BOLD, 20));
		singer.setBounds(80, 220, 50, 90);
		play.add(singer);

		title.setFont(new Font("Gothic", Font.BOLD, 13));
		title.setBounds(15, 250, 300, 70);
		play.add(title);

		lyrics.setText("<html>"
				+ "��� �ֳ��� ���� �� ��� ��⸦ <br> �״� ���� �� ���� ������ ���� �׸��� �ӿ� <br>�״븦 �ҷ������� <br>���� �� ���� ������ <br>���� ���� �� �� ���ƿ�<br>�� ���� �״븦 ���� �� ���׿�<br>�ֽᵵ �װ� �� �ȵſ�<br>������ �ݴ�� �о�� �Ҽ���<br> ����� �� �������׿�<br>����ϳ��� ���� �� ��� ��⸦<br>�״� ���� �� ���� ������<br>���� �׸��� �ӿ� �״븦 �ҷ������� <br>���� �� ���� ������ ���� ���� �� �� ���ƿ�<br>"
				+ "</html>");
		lyrics.setFont(new Font("Gothic", Font.LAYOUT_LEFT_TO_RIGHT, 12));
		lyrics.setBorder(new TitledBorder(new LineBorder(Color.black, 1)));
		lyrics.setBounds(250, 260, 300, 240);
		play.add(lyrics);
	}

	void mainVol() { // ���� ���� ���� ��ư �Լ� ����
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

	void mainMenu() { // ���� �޴� ��ư �Լ� ����

		JMenuBar menubar = new JMenuBar(); // �޴��� ����
		JMenu Mfile = new JMenu("file"); // ù��° �޴�
		JMenu Mhelp = new JMenu("option"); // �ι�° �޴�

		JMenuItem add = new JMenuItem("add");
		Mfile.add(add); // ù��° �޴��� 1�Ӽ�
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fileOpen();
				homeList();
			}
		});
		Mfile.add(new JMenuItem("delete")); // ù��° �޴��� 2�Ӽ�

		JMenuItem back = new JMenuItem("back");
		JMenuItem exit = new JMenuItem("exit");
		Mhelp.add(back); // �ι�° �޴��� 1�Ӽ�
		Mhelp.add(exit);// �ι�° �޴��� 2 �Ӽ�
		back.addActionListener(new backActionListener());
		exit.addActionListener(new MenuActionListener());

		menubar.add(Mfile); // �޴��ٿ� �ɼ��߰�
		menubar.add(Mhelp);
		frm_main.setJMenuBar(menubar);
	}

	class exitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JLabel text = new JLabel();
			int result = JOptionPane.showConfirmDialog(null, "������ �����ðڽ��ϱ�?", "EXIT", JOptionPane.YES_NO_OPTION);
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
			int result = JOptionPane.showConfirmDialog(null, "������ �����ðڽ��ϱ�?", "EXIT", JOptionPane.YES_NO_OPTION);
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
			if (btn.getText().equals("��")) {
				if (pause == 1) {
					ha.resume();
				} else {
					ha.Start();
				}
				btn.setText("||");

			} else {
				ha.pause();
				pause = 1;
				btn.setText("��");
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
			mainPlayBtn.setText("��");
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
	
	
	//��������
	class VolListener extends buttonclick1 implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			int a = mainsl.getValue();
			float b = (a/100f);
			control.setValue(b);
			System.out.println("control �� : "+control.getValue());
		}
	}

	public void fileOpen() {
		JFileChooser chooser = new JFileChooser(); // ��ü ����
		int ret = chooser.showOpenDialog(null); // ����â ����

		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "��θ� ���������ʾҽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		}

		filePath = chooser.getSelectedFile().getPath();// ���ϰ�θ� ������
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