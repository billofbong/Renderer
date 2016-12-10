package org.billofbong.renderer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.omg.CORBA.PRIVATE_MEMBER;

public class Renderer extends Canvas implements Runnable {

	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1080;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int SCALE = 3;
	
	public static final String NAME = "Renderer";
	
	private JFrame frame;
	
	public boolean running = false;
	
	public Renderer()
	{
		setSize(new Dimension(WIDTH, HEIGHT));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void run() 
	{
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int frames = 0;
		int ticks = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		while(running)
		{
			System.out.println("Running");
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			while(delta >= 1)
			{
				ticks++;
				tick();
				delta -= 1;
			}
			
			render();
		}
	}
	
	public synchronized void start()
	{
		running = true;
		new Thread(this);
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void tick()
	{
		
	}
	
	public void render()
	{
		
	}

	public static void main(String[] args)
	{
		System.out.println("Starting");
		new Renderer().start();
	}

}
