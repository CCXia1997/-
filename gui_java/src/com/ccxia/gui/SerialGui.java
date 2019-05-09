package com.ccxia.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import gnu.io.SerialPort;


public class SerialGui {
	public SerialPort s=null;
	//��Ӷ����ı�����ʾ��������
	public TextArea ta_receive=new TextArea(20,100);
	public void login()
	{
		//����iconͼ��
	    ImageIcon icon = new ImageIcon("src/resources/bookshelf.png");
		Frame f=new Frame();
		f.setTitle("ѹ���ջ�����ϵͳ");
		f.setSize(680,500);
		f.setLocation(280,130);
		f.setLayout(null);
		f.setIconImage(icon.getImage());
		//���ý�������
		Label label_title=new Label("ѹ���ջ�����ϵͳbyCCXia");
		label_title.setFont(new Font("΢���ź�",Font.BOLD,40));
		label_title.setForeground(Color.RED);
		label_title.setSize(600,50);
		label_title.setLocation(100,50);
		f.add(label_title);
		//���ô��ںţ������ʣ��趨ֵ���֣�����������ʾ
		Label label_serial=new Label("���ں�");
		Label label_baudrate=new Label("������");
		Label label_settings=new Label("�趨ֵ");
		Label label_receive=new Label("������");
		label_serial.setFont(new Font("΢���ź�",Font.BOLD,30));
		label_baudrate.setFont(new Font("΢���ź�",Font.BOLD,30));
		label_settings.setFont(new Font("΢���ź�",Font.BOLD,30));
		label_receive.setFont(new Font("΢���ź�",Font.BOLD,20));
		label_serial.setSize(100,30);
		label_serial.setLocation(100,120);
		label_baudrate.setSize(100,30);
		label_baudrate.setLocation(100,180);
		label_settings.setSize(100,30);
		label_settings.setLocation(100,240);
		label_receive.setSize(80,20);
		label_receive.setLocation(400,110);
		
		f.add(label_serial);
		f.add(label_baudrate);
		f.add(label_settings);
		f.add(label_receive);
		
		//���ô��ںţ������ʣ��趨ֵ���ı�������棬����ʽ�˵������鷳������
		final TextField tf_serial=new TextField(20);
		final TextField tf_baudrate=new TextField(20);
		final TextField tf_settings=new TextField(20);
		tf_serial.setSize(100,30);
		tf_serial.setLocation(200,122);
		tf_baudrate.setSize(100,30);
		tf_baudrate.setLocation(200,182);
		tf_settings.setSize(100,30);
		tf_settings.setLocation(200,242);
		
		f.add(tf_serial);
		f.add(tf_baudrate);
		f.add(tf_settings);
		
		//���ý�����
		ta_receive.setSize(200,330);
		ta_receive.setLocation(350,140);
		f.add(ta_receive);
		
		
		//���ô򿪴��ڣ��رմ��ڣ����ƿ�ʼ��ť
		Button bu_open=new Button("�򿪴���");
		Button bu_close=new Button("�رմ���");
		Button bu_start=new Button("��ʼ����");
		bu_open.setSize(200,50);
		bu_open.setLocation(100,300);
		bu_close.setSize(200,50);
		bu_close.setLocation(100,360);
		bu_start.setSize(200,50);
		bu_start.setLocation(100,420);
		
		//���ð�ť�¼�
		bu_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//���ı����л�ȡ��Ӧ�Ĵ��ںźͲ�����
				String portName=tf_serial.getText().trim();
				int baudrate=Integer.valueOf(tf_baudrate.getText().trim());
				s=SerialTool.openPort(portName,baudrate);
			}
		});
		
		bu_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SerialTool.closePort(s);
			}
		});
		
		bu_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//����ͼ����
				//����һ������x��y���ݵĶ���
				XYSeries series=new XYSeries("xySeries");
				int receive_data;
				int settings=Integer.valueOf(tf_settings.getText().trim());
				for(int i=0;i<50;i++)
				{
					try {
						SerialTool.sendToPort(s,settings);
						settings++;
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					receive_data=SerialTool.readFromPort(s);
					ta_receive.append("ʱ�䣺"+String.format("%.1f",(i+1)*0.2)+"s    "+String.valueOf(receive_data)+"\r\n");
					//��series��ӣ�x��y������
					series.add((i+1)*0.2,receive_data);
				}
				//ѭ�������Ժ�������ݽ��л�ͼ
				XYSeriesCollection dataset = new XYSeriesCollection();
				dataset.addSeries(series);
				//����ͼ��Ļ�������
				JFreeChart chart = ChartFactory.createXYLineChart("��ѹ�仯����ͼ","ʱ��/s","��ѹֵ",dataset,PlotOrientation.VERTICAL,false,false,false);
				ChartFrame frame = new ChartFrame("my picture", chart);
				frame.pack();
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		f.add(bu_open);
		f.add(bu_close);
		f.add(bu_start);
		
		
		//�ô��ڹر�
		//����������Ľ�
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		
		
		
		
		
		
		
		
		
		f.setVisible(true);
	}
	
	//����ͼ��
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
