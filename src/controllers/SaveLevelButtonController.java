package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JPanel;
import entities.Model;
import entities.Word;

public class SaveLevelButtonController implements ActionListener {

	private Model m;
	private JPanel contentPane;
	
	public SaveLevelButtonController(Model m, JPanel contentPane){
		this.m = m;
		this.contentPane = contentPane;
	}

	// NOTE: Don't test this...
	@Override
	public void actionPerformed(ActionEvent ae) {
		File saveFile = new FilePicker(contentPane).save();
		if (saveFile != null) {
			String filename = saveFile.getAbsolutePath(); 
			this.saveLevel(filename);
		}
	}
	
	/*
	 * Saves a textual representation of the game's board.
	 * If a tile is active, it will store the tile's character.
	 * If a tile is inactive, it will store the sentinel value '!'.
	 */
	public void saveLevel(String filename){
		System.out.println("Saving file: " + filename);
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			
			
			out.write(m.getBoard().serialize().getBytes());
			out.write('\n');
			
			
		
			out.write(m.getScore().serialize().getBytes());
			out.write('\n');
			
			for(Object w : m.getWordListModel().toArray()){
				String s = (String)w;
				out.write((s+" ").getBytes());
			}
			out.write('\n');
			
			out.write(m.getTitle().getBytes());
			out.write('\n');
			
			//out.write(m.getType().getBytes());
			out.write('\n');
			
			out.write('\n');
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
