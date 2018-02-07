package client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import exceptions.NoMatchingAssessment;
import exceptions.UnauthorizedAccess;
import interfaces.Assessment;
import interfaces.ExamServer;
import interfaces.Question;

public class Client {

	private ArrayList<Assessment> assessments;
	private ArrayList<Question> allQuestions;
	private String[] answerOptions;
	private static int token;
	private String username;
	private static ExamServer stub;
	private List<String> summaries;
	public static void main(String[] args) {

		GUI();

		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			stub = (ExamServer) registry.lookup("ExamServer");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}

	}

	public static void GUI() {
		JFrame frame = new JFrame("Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		BoxLayout layout = new BoxLayout(contentPane, 1);
		contentPane.setLayout(layout);
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		String[] ass = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		String[] assignments = { "Math", "English", "Irish" };
		// login
		JLabel usernameLabel = new JLabel("Username");
		JLabel passwordLabel = new JLabel("Password");
		JTextArea username = new JTextArea("");
		username.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JTextArea password = new JTextArea("");
		password.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JButton login = new JButton("Login");

		// assessment summary
		JTextArea assessmentSummary = new JTextArea("");
		assessmentSummary.setEditable(false);
		assessmentSummary
				.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JButton summary = new JButton("Get Assessment Summaries");

		// select assessment
		JLabel selectAssessment = new JLabel("Select Assessment");
		JComboBox assessments = new JComboBox(ass);
		DefaultComboBoxModel assessmentsModel = (DefaultComboBoxModel) assessments.getModel();

		// select questions
		JLabel selectQuestion = new JLabel("Select Question");
		JComboBox qs = new JComboBox(ass);
		DefaultComboBoxModel questionsModel = (DefaultComboBoxModel) qs.getModel();
		JLabel selectAnswer = new JLabel("Select Answer");
		JComboBox answers = new JComboBox(ass);
		DefaultComboBoxModel answersModel = (DefaultComboBoxModel) answers.getModel();

		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// login
				String pass = password.getText();
				int name = Integer.valueOf(username.getText());
				System.out.println(name + " " + pass);

				try {
					token = stub.login(name, pass);
					System.out.println(token);
					List<String> summaries = stub.getAvailableSummary(token, name);
				} catch (RemoteException e1) {
					System.out.println(e1.getMessage());
				} catch (UnauthorizedAccess e1) {
					System.out.println(e1.getMessage());
				} catch (NoMatchingAssessment e1) {
					System.out.println(e1.getMessage());
				}
				
				// get all assignments
				// updates content of assessments combobox
				assessmentsModel.removeAllElements();
				for (String item : assignments) {
					assessmentsModel.addElement(item);
				}
				assessments.setModel(assessmentsModel);
			}
		});

		assessments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selected = assessments.getSelectedIndex();

				// get assessment coursecode from summary

				// get assessment

				// get all assignments
				// updates content of assessments combobox
				questionsModel.removeAllElements();
				for (String item : assignments) {
					questionsModel.addElement(item);
				}
				qs.setModel(questionsModel);
			}
		});

		qs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selected = qs.getSelectedIndex();

				// get assessment coursecode from summary

				// get assessment

				// get all assignments
				// updates content of assessments combobox
				answersModel.removeAllElements();
				for (String item : assignments) {
					answersModel.addElement(item);
				}
				answers.setModel(answersModel);
			}
		});

		contentPane.add(usernameLabel);
		contentPane.add(username);
		contentPane.add(Box.createRigidArea(new Dimension(5, 10)));
		contentPane.add(passwordLabel);
		contentPane.add(password);
		contentPane.add(Box.createRigidArea(new Dimension(5, 10)));
		contentPane.add(login);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(assessmentSummary);
		contentPane.add(Box.createRigidArea(new Dimension(5, 10)));
		contentPane.add(summary);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(selectAssessment);
		contentPane.add(assessments);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(selectQuestion);
		contentPane.add(qs);
		contentPane.add(selectAnswer);
		contentPane.add(answers);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		frame.setVisible(true);
		frame.setSize(400, 600);
	}
}
