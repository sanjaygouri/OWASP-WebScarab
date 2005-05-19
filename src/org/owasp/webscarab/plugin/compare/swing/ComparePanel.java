/*
 * ComparePanel.java
 *
 * Created on 18 May 2005, 06:15
 */

package org.owasp.webscarab.plugin.compare.swing;

import org.owasp.webscarab.model.ConversationID;
import org.owasp.webscarab.model.ConversationModel;
import org.owasp.webscarab.model.HttpUrl;
import org.owasp.webscarab.model.Response;

import org.owasp.webscarab.plugin.compare.Compare;
import org.owasp.webscarab.plugin.compare.CompareModel;

import org.owasp.webscarab.ui.swing.SwingPluginUI;
import org.owasp.webscarab.ui.swing.ConversationListModel;
import org.owasp.webscarab.ui.swing.ConversationListTableModel;
import org.owasp.webscarab.ui.swing.ContentPanel;

import org.owasp.webscarab.util.swing.ColumnDataModel;
import org.owasp.webscarab.util.swing.TableSorter;
import org.owasp.webscarab.util.swing.ListComboBoxModel;

import javax.swing.JPanel;
import javax.swing.Action;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author  rogan
 */
public class ComparePanel extends javax.swing.JPanel implements SwingPluginUI {
    
    private Compare _compare;
    private CompareModel _model;
    private ConversationListTableModel _tableModel;
    private ContentPanel _baseContent;
    private ContentPanel _selectedContent;
    private TableSorter _conversationSorter;
    
    private Logger _logger = Logger.getLogger(getClass().getName());
    
    /** Creates new form ComparePanel */
    public ComparePanel(Compare compare) {
        initComponents();
        _compare = compare;
        _model = _compare.getModel();
        baseComboBox.setModel(new ListComboBoxModel(new ConversationListModel(_model.getConversationModel())));
        _tableModel = new ConversationListTableModel(_model.getComparisonModel());
        _conversationSorter = new TableSorter(_tableModel, conversationTable.getTableHeader());
        conversationTable.setModel(_conversationSorter);
        _baseContent = new ContentPanel();
        _selectedContent = new ContentPanel();
        contentsSplitPane.setLeftComponent(_baseContent);
        contentsSplitPane.setRightComponent(_selectedContent);
    
        baseComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _logger.info("Performed");
                Object o = baseComboBox.getSelectedItem();
                if (o instanceof ConversationID) {
                    ConversationID id = (ConversationID) o;
                    _logger.info("Got " + id);
                    _model.setBaseConversation(id, null);
                    _logger.info("setBase");
                    ConversationModel cmodel = _model.getConversationModel();
                    _logger.info("gotConversaiont");
                    Response response = cmodel.getResponse(id);
                    _logger.info("gotResponse");
                    String contentType = response.getHeader("Content-Type");
                    _logger.info("Content-Type is " + contentType);
                    _baseContent.setContentType(contentType);
                    _logger.info("setType");
                    _baseContent.setContent(response.getContent());
                    _logger.info("setcontent");
                }
            }
        });
        
        conversationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                int selected = conversationTable.getSelectedRow();
                if (selected == -1) {
                    _selectedContent.setContent(null);
                    return;
                }
                selected = _conversationSorter.modelIndex(selected);
                ConversationModel cmodel = _model.getComparisonModel();
                ConversationID id = cmodel.getConversationAt(null, selected);
                Response response = cmodel.getResponse(id);
                String contentType = response.getHeader("Content-Type");
                _selectedContent.setContentType(contentType);
                _selectedContent.setContent(response.getContent());
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        baseComboBox = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        conversationTable = new javax.swing.JTable();
        contentsSplitPane = new javax.swing.JSplitPane();

        setLayout(new java.awt.BorderLayout());

        add(baseComboBox, java.awt.BorderLayout.NORTH);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.3);
        jSplitPane1.setOneTouchExpandable(true);
        conversationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(conversationTable);

        jSplitPane1.setLeftComponent(jScrollPane1);

        contentsSplitPane.setResizeWeight(0.5);
        contentsSplitPane.setOneTouchExpandable(true);
        jSplitPane1.setRightComponent(contentsSplitPane);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    public Action[] getConversationActions() {
        return new Action[0];
    }    
    
    public ColumnDataModel[] getConversationColumns() {
        return new ColumnDataModel[0];
    }    
    
    public JPanel getPanel() {
        return this;
    }
    
    public String getPluginName() {
        return "Compare";
    }
    
    public Action[] getUrlActions() {
        return new Action[0];
    }
    
    public ColumnDataModel[] getUrlColumns() {
        return new ColumnDataModel[0];
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseComboBox;
    private javax.swing.JSplitPane contentsSplitPane;
    private javax.swing.JTable conversationTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
    
}
