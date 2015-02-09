package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;

import model.Pool;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OverviewPanel overviewPanel;
	private Component currentContent;
	
	private Pool puzzle;
	
	public MainFrame(Pool puzzle) {
		this.puzzle = puzzle;
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		
		overviewPanel = new OverviewPanel(puzzle);
		
		setContent(overviewPanel);

		setSize(605, 660);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Backtracking");
		setLocationRelativeTo(null);
	}



	public void setContent(Component component) {
		Container contentPane = getContentPane();
		if (currentContent != null) {
			contentPane.remove(currentContent);
		}
		contentPane.add(component, BorderLayout.CENTER);
		currentContent = component;
		contentPane.doLayout();
		repaint();
	}

}
