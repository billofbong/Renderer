package org.billofbong.renderer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Renderer extends Canvas implements Runnable {

	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1080;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int SCALE = 3;
	
	public static final String NAME = "Renderer";
	
	private JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage newImage = image;
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	Random random = new Random();
	
	public Renderer()
	{
		try {
			image = ImageIO.read(new File("images/minion.jpg"));
			newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			newImage.getGraphics().drawImage(image, 0, 0, null);
			pixels = ((DataBufferInt) newImage.getRaster().getDataBuffer()).getData();
		} catch (IOException e) {
			e.printStackTrace();
		}		
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
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;
			
			while(delta >= 1)
			{
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			if(shouldRender)
			{
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= (1000 * 0.416666))
			{
				lastTimer += 1000;
				System.out.println("FPS: " + frames + ", " + "Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public synchronized void start()
	{
		running = true;
		new Thread(this).start();
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void tick()
	{
		tickCount++;
		int speed = 1;
		for(int i = 0; i < pixels.length; i++)
		{
			if(i + speed < pixels.length)
			{	
			pixels[i] = pixels[i + speed];
			}
			//System.out.println(i);
		}
	}
	
	public void render()
	{
		BufferStrategy bufferStrategy = getBufferStrategy();
		if(bufferStrategy == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = bufferStrategy.getDrawGraphics();
		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, getWidth(), getHeight());
		graphics.drawImage(newImage, 0, 0, getWidth(), getHeight(), null);
		graphics.dispose();
		bufferStrategy.show();
	}

	public static void main(String[] args)
	{
		System.out.println("Starting");
		new Renderer().start();
	}

}
