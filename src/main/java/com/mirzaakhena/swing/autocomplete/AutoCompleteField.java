package com.mirzaakhena.swing.autocomplete;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mirzaakhena.swing.basetablemodel.BaseTableModel;

/**
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Mirza Akhena
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author mirza.akhena@gmail.com
 *
 * @param <T>
 */
public class AutoCompleteField<T> extends JTextField implements DocumentListener {

	private static final long serialVersionUID = 4801105020420402460L;

	private JTable table;
	private Searchable<T> searchable;
	private T selectedObject;
	private MyPopupMenu myPopupMenu;
	private boolean isSearch;

	public AutoCompleteField() {

		table = new JTable();
		JScrollPane scrollPane = new JScrollPane();
		myPopupMenu = new MyPopupMenu();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(20);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					selectItem();
				}
			}
		});

		myPopupMenu.add(scrollPane);
		scrollPane.setViewportView(table);

		getDocument().addDocumentListener(this);
		isSearch = true;
	}

	/**
	 * expose the popup table
	 * 
	 * @return
	 */
	public JTable getPopupTable() {
		return table;
	}

	/**
	 * set the searchable object and tablemodel
	 * 
	 * @param searchable
	 * @param tableModel
	 */
	public void setSearchable(Searchable<T> searchable, BaseTableModel<T> tableModel) {
		this.searchable = searchable;
		table.setModel(tableModel);
	}

	private void down() {
		if (table.getSelectedRow() < table.getRowCount() - 1) {
			int index = table.getSelectedRow();
			table.getSelectionModel().removeSelectionInterval(index, index);
			table.getSelectionModel().addSelectionInterval(index + 1, index + 1);
			table.scrollRectToVisible(table.getCellRect(index + 1, 0, true));
		}
	}

	private void up() {
		if (table.getSelectedRow() > 0) {
			int index = table.getSelectedRow();
			table.getSelectionModel().removeSelectionInterval(index, index);
			table.getSelectionModel().addSelectionInterval(index - 1, index - 1);
			table.scrollRectToVisible(table.getCellRect(index - 1, 0, true));
		}
	}

	@Override
	protected void processKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_RELEASED) {

			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_DOWN) {
				if (table.getSelectedRow() == -1) {
					table.getSelectionModel().addSelectionInterval(0, 0);
				} else {
					down();
				}
				e.consume();
				moveCaret();
				return;
			}

			if (keyCode == KeyEvent.VK_UP) {
				if (table.getSelectedRow() == 0) {
					myPopupMenu.setVisible(false);
				} else {
					up();
				}
				e.consume();
				moveCaret();
				return;
			}

			if (keyCode == KeyEvent.VK_ENTER) {
				if (myPopupMenu.isVisible()) {
					selectItem();
					e.consume();
				}
				return;
			}

			if (keyCode == KeyEvent.VK_ESCAPE) {
				if (selectedObject != null && !searchable.getStringValue(selectedObject).equals(getText())) {
					selectedObject = null;
					searchable.selectEvent(selectedObject);

				}
			}

		}

		super.processKeyEvent(e);
	}

	private void selectItem() {
		int index = table.getSelectedRow();
		if (index >= 0) {
			selectedObject = getTableModel().getItem(index);
			setSelectedItem(selectedObject);
		}
	}

	/**
	 * 
	 * set default selected object
	 * 
	 * @param selectedObject
	 */
	public void setSelectedItem(T selectedObject) {
		if (selectedObject != null) {
			isSearch = false;
			setText(searchable.getStringValue(selectedObject));
			isSearch = true;
			SwingUtilities.invokeLater(() -> {
				setCaretPosition(getText().length());
				myPopupMenu.setVisible(false);
			});
		}
		searchable.selectEvent(selectedObject);
	}

	private class MyPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 3226727071126854831L;

		@Override
		public void show(Component invoker, int x, int y) {
			super.show(invoker, x, y);
			SwingUtilities.invokeLater(() -> invoker.requestFocus());
		}

		@Override
		public void setVisible(boolean b) {
			super.setVisible(b);
			if (!b) {
				getTableModel().deleteAll();
			}
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (isSearch) {
			SwingUtilities.invokeLater(() -> showPopup());
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (isSearch) {
			SwingUtilities.invokeLater(() -> showPopup());
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

	@Override
	protected void processFocusEvent(FocusEvent e) {
		super.processFocusEvent(e);
		if (e.getID() == FocusEvent.FOCUS_LOST) {
			if (myPopupMenu.isVisible()) {
				requestFocus();
				moveCaret();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public BaseTableModel<T> getTableModel() {
		return (BaseTableModel<T>) table.getModel();
	}

	public Dimension getPopupTableSize() {
		return new Dimension(getWidth(), 200);
	}

	public Point getPopupTablePosition() {
		return new Point(0, getHeight());
	}

	private void moveCaret() {
		SwingUtilities.invokeLater(() -> setCaretPosition(getText().length()));
	}

	private void showPopup() {

		BaseTableModel<T> tableModel = getTableModel();

		if (searchable != null && tableModel != null) {

			String text = getText();

			if (text.length() > 0) {

				List<T> founds = searchable.search(text);
				if (founds != null && !founds.isEmpty()) {

					tableModel.setItem(founds);

					table.setRowSelectionInterval(0, 0);

					if (!myPopupMenu.isVisible()) {
						myPopupMenu.setPreferredSize(getPopupTableSize());
						Point p = getPopupTablePosition();
						myPopupMenu.show(this, p.x, p.y);
						table.scrollRectToVisible(table.getCellRect(0, 0, true));
					}

				} else {
					myPopupMenu.setVisible(false);
					moveCaret();
					searchable.selectEvent(null);
				}

			} else if (myPopupMenu.isVisible()) {
				myPopupMenu.setVisible(false);
				moveCaret();
				searchable.selectEvent(null);
			}

		}

	}

}
