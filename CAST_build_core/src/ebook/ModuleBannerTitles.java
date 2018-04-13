package ebook;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.font.*;
import javax.swing.*;

import ebookStructure.*;


public class ModuleBannerTitles extends CoreBannerTitles {
	
	private JLabel ch, chNo, page;
	private UiImage lineImage;
	
	public ModuleBannerTitles(BookFrame theWindowParam) {
		super(theWindowParam);
		
		Font chFont = new Font("Arial", Font.PLAIN, 24);
		chFont = chFont.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT,
																												TextAttribute.WEIGHT_SEMIBOLD));
		
		Font pageFont = new Font("Arial", Font.PLAIN, 36);
		pageFont = pageFont.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT,
																												TextAttribute.WEIGHT_SEMIBOLD));
													// semi-bold looks same as plain font unfortunately
		
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		
			GridBagConstraints logoC = new GridBagConstraints();
			logoC.anchor = GridBagConstraints.WEST;
			logoC.fill = GridBagConstraints.NONE;
			logoC.gridheight = 3;
			logoC.gridwidth = 1;
			logoC.gridx = 0;
			logoC.gridy = 0;
			logoC.insets = new Insets(6,2,6,30);
			logoC.ipadx = logoC.ipady = 0;
			logoC.weightx = 0.0;
			logoC.weighty = 0.0;
			
			File structureDir = theWindowParam.getEbook().getStructureDir();
			File logoFile = new File(structureDir, "images/logo.png");
		UiImage logoImage = new UiImage(logoFile, true);
		add(logoImage);
			gbl.setConstraints(logoImage, logoC);
		
		
			GridBagConstraints chapterNoC = new GridBagConstraints();
			chapterNoC.anchor = GridBagConstraints.WEST;
			chapterNoC.fill = GridBagConstraints.NONE;
			chapterNoC.gridheight = 3;
			chapterNoC.gridwidth = 1;
			chapterNoC.gridx = 1;
			chapterNoC.gridy = 0;
			chapterNoC.insets = new Insets(0,20,0,20);
			chapterNoC.ipadx = chapterNoC.ipady = 0;
			chapterNoC.weightx = 0.0;
			chapterNoC.weighty = 0.0;
		
//		chNo = new JLabel("2", JLabel.LEFT);
		chNo = new JLabel("", JLabel.LEFT);
		chNo.setFont(new Font("Times New Roman", Font.PLAIN, 64));
		chNo.setForeground(kGreyTextColor);
		add(chNo);
			gbl.setConstraints(chNo, chapterNoC);
		
		
			GridBagConstraints chapterNameC = new GridBagConstraints();
			chapterNameC.anchor = GridBagConstraints.SOUTHWEST;
			chapterNameC.fill = GridBagConstraints.NONE;
			chapterNameC.gridheight = 1;
			chapterNameC.gridwidth = 1;
			chapterNameC.gridx = 2;
			chapterNameC.gridy = 0;
			chapterNameC.insets = new Insets(10,0,0,0);
			chapterNameC.ipadx = chapterNameC.ipady = 0;
			chapterNameC.weightx = 1.0;
			chapterNameC.weighty = 0.0;
		
//		sect = new JLabel("3. Histograms and density", JLabel.LEFT);
		ch = new JLabel("", JLabel.LEFT);
		ch.setFont(chFont);
		ch.setForeground(kGreyTextColor);
		add(ch);
			gbl.setConstraints(ch, chapterNameC);
		
		
			GridBagConstraints pageC = new GridBagConstraints();
			pageC.anchor = GridBagConstraints.WEST;
			pageC.fill = GridBagConstraints.NONE;
			pageC.gridheight = 1;
			pageC.gridwidth = 1;
			pageC.gridx = 2;
			pageC.gridy = 1;
			pageC.insets = new Insets(2,30,2,0);
			pageC.ipadx = pageC.ipady = 0;
			pageC.weightx = 0.0;
			pageC.weighty = 0.0;
		
//		page = new JLabel("1. Density of values", JLabel.LEFT);
		page = new JLabel("", JLabel.LEFT);
		page.setFont(pageFont);
		page.setForeground(kBlackTextColor);
		add(page);
			gbl.setConstraints(page, pageC);
		
		
			GridBagConstraints lineC = new GridBagConstraints();
			lineC.anchor = GridBagConstraints.NORTHWEST;
			lineC.fill = GridBagConstraints.NONE;
			lineC.gridheight = 1;
			lineC.gridwidth = 1;
			lineC.gridx = 2;
			lineC.gridy = 2;
			lineC.insets = new Insets(0,0,4,0);
			lineC.ipadx = lineC.ipady = 0;
			lineC.weightx = 0.0;
			lineC.weighty = 1.0;
			
		File lineFile = new File(structureDir, "images/pageUnderscore.png");
		lineImage = new UiImage(lineFile, false);
		add(lineImage);
			gbl.setConstraints(lineImage, lineC);
		
		updateTitles();
	}
	
	public void updateTitles() {
		DomElement e = theWindow.currentElement;
		String[] titles = e.getTitles();
		chNo.setText((titles[0] == null) ? "" : titles[0]);
		ch.setText((titles[1] == null) ? "" : titles[1]);
		page.setText((titles[2] == null) ? "" : titles[2]);
		
		lineImage.setVisible(titles[2] != null);
	}
}