package fluke.waveswing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import fluke.waveswing.wfc.Map;
import fluke.waveswing.wfc.TileSet;

public class Gui  implements MouseListener 
{
	public static int canvasWidth = 1000;
	public static int canvasHeight = 600;
	public static boolean reset = false;
	public static boolean runSlow = false;
	public static boolean giveTheFuckUp = false;
	
	public Gui() throws InterruptedException
	{
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel painterPane = new JPanel();
		painterPane.setLayout(new BoxLayout(painterPane, BoxLayout.Y_AXIS));
		PaintCanvas painter = new PaintCanvas();
		painterPane.add(painter);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		JButton bReset = new JButton("Restart");
		JButton bStop = new JButton("Stop");
		JCheckBox slowCheck = new JCheckBox("Slow Down");
		buttonPane.add(bReset);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(bStop);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(slowCheck);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
		
		JPanel grouphug = new JPanel();
		grouphug.setLayout(new BoxLayout(grouphug, BoxLayout.Y_AXIS));
		grouphug.add(painterPane);
		grouphug.add(buttonPane);
		
		f.add(grouphug);
		//f.addMouseListener(this);
		f.setSize(canvasWidth, canvasHeight);
		f.setVisible(true);
		
		
		bReset.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//will restart new run when current one is finished
				reset = true;			
			}          
		});
		
		bStop.addActionListener(new ActionListener() 
		{ 
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//end current run
				giveTheFuckUp = true;			
			}          
		});
		
		slowCheck.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				//adds delay in between placing tiles
				runSlow = e.getStateChange() == 1 ? true : false;
			}
		});

		for(;;)
		{
			if(reset)
			{
				Main.reset();
				reset = false;
				giveTheFuckUp = false;
			}
			
			while(!Main.wfc.allTilesSet() && !giveTheFuckUp) 
			{
				if(runSlow)
					Thread.sleep(10);
				
				if(Main.wfc.collapseNext())
				{
					painter.repaint();
				}
				else //no tile was possible at location, try again
				{
					reset = true;
					giveTheFuckUp = true;
				}
			}
			Thread.sleep(100);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

}
