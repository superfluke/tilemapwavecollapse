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
	public static int canvasWidth = 800;
	public static int canvasHeight = 600;
	public static boolean reset = false;
	public static boolean runSlow = false;
	
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
		JButton b2 = new JButton("hi");
		JCheckBox slowCheck = new JCheckBox("Slow Down");
		buttonPane.add(bReset);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(b2);
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
				reset = true;			
			}          
		});
		
		slowCheck.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				runSlow = e.getStateChange() == 1 ? true : false;
			}
		});
		
		//wfc.initRandomMap();
		//wfc.collapseMap();
		for(;;)
		{
			if(reset)
			{
				Main.tileset = new TileSet(Main.tileSetAttempts);
				Main.wfc = new Map(Main.mapTileSize, Main.tileset);
				reset = false;
			}
			
			while(!Main.wfc.allTilesSet()) 
			{
				if(runSlow)
					Thread.sleep(30);
				
				if(Main.wfc.collapseNext())
				{
					painter.repaint();
				}
				else
				{
					reset = true;
					break;
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
