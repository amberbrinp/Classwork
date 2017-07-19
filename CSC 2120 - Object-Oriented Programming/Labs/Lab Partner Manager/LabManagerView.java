import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class LabManagerView extends CenterFrame
{
   //see further below for all instance variables, constructor, etc. (all completed for you)
   private JComboBox cboSections;
   private JComboBox cboLabs;
   private JComboBox cboStudents;
   private JComboBox cboGrades;

   private Sections sections;

   private void processGrade()
   {
      //DO THIS
      //obtain information from the combo boxes to correctly update the grade for a student (and their partner)
       int section = cboSections.getSelectedIndex()+1;
       int student = cboStudents.getSelectedIndex()+1;
       int lab = cboLabs.getSelectedIndex()+1;
       String strGrade = (String) cboGrades.getSelectedItem();
       char grade = strGrade.charAt(0);
       
       sections.setGrade(section, student, lab, grade);
       int partner = sections.getPartnerID(section, student, lab);
       if (partner != 0)
           sections.setGrade(section, student, lab, grade);
       
       
      fillTextBox();   //update the display to reflect the changes
      fillTextArea();
   }

   //no work below this point
   private void fillStudents()
   {
      cboStudents.removeAllItems();
      int sectionID = cboSections.getSelectedIndex() + 1;
      int num_students = sections.getNumStudents(sectionID);
      for (int i = 1; i <= num_students; i++)
      {
         cboStudents.addItem(sections.getStudentName(sectionID, i));
      }
      cboStudents.setSelectedIndex(0);

      fillTextBox();
   }

   private void fillLabs()
   {
      cboLabs.removeAllItems();
      int sectionID = cboSections.getSelectedIndex() + 1;
      int num_labs = sections.getNumLabs(sectionID);

      for (int i = 1; i <= num_labs; i++)
      {
         cboLabs.addItem(i + "");
      }

      cboLabs.setSelectedIndex(cboLabs.getItemCount() - 1);
   }

   //load in the new information for the new section
   private void processSection()
   {
      fillLabs();  //update all displays for new section 
      fillStudents();
      fillTextArea();
   }

   private void processInactive()
   {
      int studentID = cboStudents.getSelectedIndex() + 1;
      int sectionID = cboSections.getSelectedIndex() + 1;
      sections.setInactive(sectionID, studentID);
   }

   private void processNotPresent()
   {
      //can only be done for the current lab
      if (btnPartnerList.isEnabled())
      {
         int studentID = cboStudents.getSelectedIndex() + 1;
         int sectionID = cboSections.getSelectedIndex() + 1;
         int labID = cboLabs.getItemCount();  //can only adjust the last lab
         sections.setNotPresent(sectionID, studentID, labID);
      }
   }

   private void fillTextArea()
   {
      txtPartners.setText("");
      int labID = cboLabs.getSelectedIndex() + 1;
      int sectionID = cboSections.getSelectedIndex() + 1;
      txtPartners.setText(sections.partnerList(sectionID, labID));
   }

   private void processWriteFiles()
   {
      sections.writeFiles();  //save all information before exiting
   }

   private void processQuit()
   {
      System.exit(0);
   }

   private void fillTextBox()  
   {
      int studentID = cboStudents.getSelectedIndex() + 1;
      int sectionID = cboSections.getSelectedIndex() + 1;
      txtStudentInfo.setText(sections.studentInfo(sectionID, studentID));
   }

   private void processPartnerList()
   {
      int sectionID = cboSections.getSelectedIndex() + 1;
      sections.computePartners(sectionID);
      btnPartnerList.setEnabled(false);  //can only do this once

      btnInactive.setEnabled(false);  //once the list has been made, no more adjustments are allowed
      btnNotPresent.setEnabled(false);
      fillTextArea();  //calls partnerList

      btnQuit.setEnabled(true);
      cboLabs.setEnabled(true);
      cboLabs.addActionListener(lmc); 
      fillTextBox();

      cboSections.setEnabled(true);
      btnSave.setEnabled(true);
   }

   private void processAddLab()
   {
      int sectionID = cboSections.getSelectedIndex() + 1;
      sections.addLab(sectionID);
      int num_labs = sections.getNumLabs(sectionID);  //the newest lab is being worked on

      btnAddLab.setEnabled(false);  //can only create one lab at a time
      btnPartnerList.setEnabled(true);
      btnNotPresent.setEnabled(true);
      btnInactive.setEnabled(false);
      btnSave.setEnabled(false);

      txtPartners.setText("");
      cboLabs.removeActionListener(lmc);  //has to be here or an illegal event will be triggered
      cboLabs.setEnabled(false);
      cboLabs.addItem("" + num_labs);
      cboLabs.setSelectedIndex(num_labs - 1);

      btnQuit.setEnabled(false);
      fillTextBox();

      cboSections.setEnabled(false);
   }

private class LabManagerController implements ActionListener
{
   //a full class which can have instance variables if needed

   public void actionPerformed(ActionEvent ae)
   {
      String command = ae.getActionCommand();

      if (command.equals("Labs"))
      {
         fillTextArea();
         fillTextBox();
      }
      else if (command.equals("Students"))
      {
         fillTextBox();
      }
      else if (command.equals("Section"))
      {
         processSection();
      }
      else if (command.equals(btnSetGrade.getText()))
      {
         processGrade();
      }
      else if (command.equals(btnAddLab.getText()))
      {
         processAddLab();
      }
      else if (command.equals(btnInactive.getText()))
      {
         processInactive();
      }
      else if (command.equals(btnNotPresent.getText()))
      {
         processNotPresent();
      }
      else if (command.equals(btnPartnerList.getText()))
      {
         processPartnerList();
      }
      else if (command.equals(btnSave.getText()))
      {
         processWriteFiles();
      }
      else if (command.equals(btnQuit.getText()))
      {
         processWriteFiles();
         processQuit();
      }
   }
}

   private JTextArea txtPartners;
   private JTextField txtStudentInfo;
   
   private JButton btnAddLab;
   private JButton btnInactive;
   private JButton btnNotPresent;
   private JButton btnQuit;
   private JButton btnPartnerList;
   private JButton btnSave;

   private JButton btnSetGrade;
   private JLabel lblGrade;

   private LabManagerController lmc;

   public LabManagerView(int width, int height, String title, int num_sections, double[] grade_constants, int total_num_labs)
   {
      //set up the frame
      super(width, height, title);

      Font font = new Font("Verdana", Font.BOLD, 12);
      Font font2 = new Font("Courier New", Font.BOLD, 14);
      Font font3 = new Font("Verdana", Font.BOLD, 10);

      JPanel centerPanel = new JPanel();
      centerPanel.setBackground(Color.white);
      centerPanel.setLayout(new GridLayout(1,2));
      add(centerPanel, BorderLayout.CENTER);

      JPanel btnPanel = new JPanel();
      EasyGridBag bag = new EasyGridBag(16, 2, btnPanel);
      btnPanel.setLayout(bag);
      btnPanel.setBackground(Color.white);

      centerPanel.add(btnPanel);
      txtPartners = new JTextArea();
      txtPartners.setEditable(false);
      txtPartners.setFont(font2);

      JScrollPane scroller = new JScrollPane();
      scroller.getViewport().add(txtPartners);
      centerPanel.add(scroller);

      JLabel lblLabs = new JLabel("Lab  ");
      lblLabs.setBackground(Color.white);
      lblLabs.setFont(font);

      JLabel lblSection = new JLabel("Section  ");
      lblSection.setBackground(Color.white);
      lblSection.setFont(font);

      JLabel lblStudents = new JLabel("Student  ");
      lblStudents.setBackground(Color.white);
      lblStudents.setFont(font);

      JLabel lblGrade = new JLabel("Grade  ");
      lblGrade.setBackground(Color.white);
      lblGrade.setFont(font);

      cboLabs = new JComboBox();
      cboLabs.setEditable(false);
      cboLabs.setBackground(Color.white);
      cboLabs.setFont(font);

      cboSections = new JComboBox();
      cboSections.setEditable(false);
      cboSections.setBackground(Color.white);
      cboSections.setFont(font);
      for (int i = 1; i <= num_sections; i++)
      {
         cboSections.addItem("" + i);
      }

      cboGrades = new JComboBox();
      cboGrades.setEditable(false);
      cboGrades.setBackground(Color.white);
      cboGrades.setFont(font);
      cboGrades.addItem("A");
      cboGrades.addItem("B");
      cboGrades.addItem("C");
      cboGrades.addItem("D");
      cboGrades.addItem("F");

      Dimension dim = new Dimension(160, 27);
      cboStudents = new JComboBox();
      cboStudents.setEditable(false);
      cboStudents.setBackground(Color.white);
      cboStudents.setFont(font);
      cboStudents.setPreferredSize(dim);

      bag.fillCellAlignWithinCell(2, 1, GridBagConstraints.EAST, lblStudents);
      bag.fillCellAlignWithinCell(2, 2, GridBagConstraints.WEST, cboStudents);

      bag.fillCellAlignWithinCell(6, 1, GridBagConstraints.EAST, lblSection);
      bag.fillCellAlignWithinCell(6, 2, GridBagConstraints.WEST, cboSections);

      bag.fillCellAlignWithinCell(7, 1, GridBagConstraints.EAST, lblLabs);
      bag.fillCellAlignWithinCell(7, 2, GridBagConstraints.WEST, cboLabs);

      bag.fillCellAlignWithinCell(8, 1, GridBagConstraints.EAST, lblGrade);
      bag.fillCellAlignWithinCell(8, 2, GridBagConstraints.WEST, cboGrades);

      lmc = new LabManagerController();

      cboLabs.setActionCommand("Labs");
      cboLabs.addActionListener(lmc);

      cboStudents.setActionCommand("Students");
      cboStudents.addActionListener(lmc);

      cboSections.setActionCommand("Section");
      cboSections.addActionListener(lmc);

      btnSetGrade = processButton("   Set Grade  ", font, lmc);
      bag.fillCellCenterWithinCell(12, 2, btnSetGrade);

      btnAddLab = processButton("    Add Lab    ", font, lmc);
      bag.fillCellCenterWithinCell(13, 2, btnAddLab);

      btnPartnerList = processButton(" Partner List ", font, lmc);
      btnPartnerList.setEnabled(false);  //enabled when Add Lab has been pressed
      bag.fillCellCenterWithinCell(14, 2, btnPartnerList);

      btnNotPresent = processButton(" Not Present ", font, lmc);
      btnNotPresent.setEnabled(false);
      bag.fillCellCenterWithinCell(15, 2, btnNotPresent);

      btnInactive = processButton("    Inactive    ", font, lmc);
      btnInactive.setEnabled(true);
      bag.fillCellCenterWithinCell(16, 2, btnInactive);

      btnSave = processButton("       Save      ", font, lmc);
      bag.fillCellCenterWithinCell(15, 1, btnSave);
   
      btnQuit = processButton("       Quit       ", font, lmc);
      bag.fillCellCenterWithinCell(16, 1, btnQuit);

      txtStudentInfo = new JTextField();
      txtStudentInfo.setEditable(false);
      bag.fillCellWithRowColSpan(1, 1, 1, 2, GridBagConstraints.HORIZONTAL, txtStudentInfo);
      txtStudentInfo.setFont(font3);

      sections = new Sections(num_sections, grade_constants, total_num_labs);
      processSection();  //important stuff done here

      //txtStudentInfo.setVisible(false);  //don't need this component any more
      cboLabs.setSelectedIndex(cboLabs.getItemCount() - 1);
      addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent event) { System.exit(0);} });
      setVisible(true);
   }

   private JButton processButton(String label, Font font, LabManagerController lmc)
   {
      JButton btn = new JButton(label);
      btn.setBackground(Color.white);
      btn.addActionListener(lmc);
      btn.setActionCommand(label);
      btn.setFont(font);
      return btn;
   }
}