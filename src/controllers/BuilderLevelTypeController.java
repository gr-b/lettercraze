package controllers;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import builderBoundary.BuildField;
import entities.Model;

public class BuilderLevelTypeController implements ActionListener {

	private BuildField buildField;
	private Model m;
	private JComboBox<String> lt;

	public BuilderLevelTypeController(BuildField buildField, Model m, JComboBox<String> lt) {
		this.buildField = buildField;
		this.m = m;
		this.lt = lt;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Handles timefield
		int index = lt.getSelectedIndex();
		if ((index == 0) || (index == 2)){
			JTextField tempField = buildField.getTimeField();
			tempField.setEnabled(false);
			buildField.setTimeField(tempField);
		}
		else {
			JTextField tempField = buildField.getTimeField();
			tempField.setEnabled(true);
			buildField.setTimeField(tempField);
		}
		
		//Handles worldlist
		if(index != 2) {
			JTextField tempWordField = buildField.getWordEntryField();
			tempWordField.setEnabled(false);
			buildField.setWordEntryField(tempWordField);
			int size = m.getWordListModel().getSize();
			//Clears list
			for (int i = 0; i < size; i++) {
			m.removeWordListModel(0); }
			JButton tempButton = buildField.getRemoveButton();
			tempButton.setEnabled(false);
	        buildField.setRemoveButton(tempButton);
		}
		else {
			JTextField tempWordField = buildField.getWordEntryField();
			tempWordField.setEnabled(true);
			buildField.setWordEntryField(tempWordField);
		}
		
		// Update the model
		// 0 - puzzle, 1 - lightning, 2 -theme
		m.setType(index);

	}
}

