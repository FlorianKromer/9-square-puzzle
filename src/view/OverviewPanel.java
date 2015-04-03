package view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Piece;
import model.Pool;
import model.PoolSizeException;
import run.MainView;

public class OverviewPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Pool puzzle;
	/**
	 * position in the solution list
	 */
	private int positionList;
	/**
	 * button for switch
	 */
	JButton nextButton, previousButton;
	/**
	 * display the position
	 */
	JLabel positionText;
	/**
	 * for interface only
	 */
	JPanel gui;

	/**
	 * file chooser
	 */
	JButton openButton;

	/**
	 * constructeur
	 */
	public OverviewPanel(Pool puzzle) {
		setLayout(new BorderLayout());
		this.puzzle = puzzle;
		nextButton = new JButton("Next");
		previousButton = new JButton("Previous");
		gui = new JPanel();
		positionList = 1;
		openButton = new JButton("Parcourir");
		positionText = new JLabel();

		initGUI();

		gui.add(openButton);
		gui.add(previousButton);
		gui.add(nextButton);

		gui.add(positionText);
		this.add(gui, BorderLayout.SOUTH);
	}

	/**
	 * create the interface for switch between answers
	 */
	private void initGUI() {
		previousButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (positionList > 1) {
					positionList--;
					positionText.setText(positionList + " / "
							+ puzzle.getSolutions().size());
					repaint();

				}
			}
		});

		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (positionList < puzzle.getSolutions().size()) {
					positionList++;
					positionText.setText(positionList + " / "
							+ puzzle.getSolutions().size());
					repaint();
				}
			}
		});
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System
						.getProperty("user.dir")));
				int option = chooser.showOpenDialog(OverviewPanel.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					try {
						UpdatePuzzle(chooser.getSelectedFile()
								.getAbsolutePath());
					} catch (PoolSizeException e) {
						e.printStackTrace();
					}

				} else {
					JOptionPane.showMessageDialog(null, "alert");
				}
			}
		});

		positionText.setText(positionList + " / "
				+ puzzle.getSolutions().size());

	}

	public void UpdatePuzzle(String path) throws PoolSizeException {
		Pool p = new Pool();
		p.load(path);
		p.isPerfect();
		this.puzzle = p;
		initGUI();
		repaint();

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);

		int x = 0, y = 0, i = 0;

		for (Piece it : puzzle.getSolutions().get(positionList - 1)) {
			g.drawRect(x, y, MainView.PIECE, MainView.PIECE);
			g.drawString(it.getLetter(), (int) (x + MainView.PIECE * 0.45),
					(int) (y + MainView.PIECE * 0.5));
			g.drawString(it.getTop() + "", (int) (x + MainView.PIECE * 0.45),
					(int) (y + MainView.PIECE * 0.2));
			g.drawString(it.getBottom() + "",
					(int) (x + MainView.PIECE * 0.45),
					(int) (y + MainView.PIECE * 0.9));
			g.drawString(it.getLeft() + "", (int) (x + MainView.PIECE * .1),
					(int) (y + MainView.PIECE * 0.5));
			g.drawString(it.getRight() + "", (int) (x + MainView.PIECE * 0.8),
					(int) (y + MainView.PIECE * 0.5));

			x = x + MainView.PIECE;
			i++;
			if (i % puzzle.getSize() == 0) {
				x = 0;
				y = y + MainView.PIECE;
			}
		}
	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		super.repaint();

	}

	public Pool getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(Pool puzzle) {
		this.puzzle = puzzle;
	}

}
