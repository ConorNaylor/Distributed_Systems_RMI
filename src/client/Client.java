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

import exceptions.InvalidOptionNumber;
import exceptions.InvalidQuestionNumber;
import exceptions.NoMatchingAssessment;
import exceptions.UnauthorizedAccess;
import interfaces.Assessment;
import interfaces.ExamServer;
import interfaces.Question;

public class Client {

	private ArrayList<Assessment> assessments;
	private ArrayList<Question> allQuestions;
	private String[] answerOptions;
	private static long token;
	private String username;
	private static ExamServer stub;
	private static List<String> summaries;
	private static ArrayList<String> coursecodes;
	private static int studentid;
	private static Assessment assessment;
	private static Question question;
	private static int selectedQuestion;

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

		// login
		JLabel usernameLabel = new JLabel("Username");
		JLabel passwordLabel = new JLabel("Password");
		JTextArea username = new JTextArea("");
		username.setSize(100, 30);
		username.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JTextArea password = new JTextArea("");
		password.setSize(100, 30);
		password.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JButton login = new JButton("Login");

		// assessment summary
		JTextArea assessmentSummary = new JTextArea("");
		assessmentSummary.setEditable(false);
		assessmentSummary
				.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		JLabel summary = new JLabel("Assessment Summaries");

		// select assessment
		JLabel selectAssessment = new JLabel("Select Assessment");
		JComboBox assessments = new JComboBox();
		DefaultComboBoxModel assessmentsModel = (DefaultComboBoxModel) assessments.getModel();

		// select questions
		JLabel selectQuestion = new JLabel("Select Question");
		JComboBox qs = new JComboBox();
		DefaultComboBoxModel questionsModel = (DefaultComboBoxModel) qs.getModel();
		JLabel selectAnswer = new JLabel("Select Answer");
		JComboBox answers = new JComboBox();
		DefaultComboBoxModel answersModel = (DefaultComboBoxModel) answers.getModel();

		JButton submitQuestion = new JButton("Submit Question Answer");

		JButton submit = new JButton("Submit Assessment");
		
		JTextArea info = new JTextArea("");
		info.setEditable(false);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// login
				String pass = password.getText();
				studentid = Integer.valueOf(username.getText());

				try {
					token = stub.login(studentid, pass);
					summaries = stub.getAvailableSummary(token, studentid);
				} catch (RemoteException e1) {
					System.out.println(e1.getMessage());
				} catch (UnauthorizedAccess e1) {
					System.out.println(e1.getMessage());
				} catch (NoMatchingAssessment e1) {
					System.out.println(e1.getMessage());
				}

				coursecodes = new ArrayList<String>();

				assessmentSummary.setText("");
				for (String item : summaries) {
					assessmentSummary.append(item + "\n");
					String coursecode = item.substring(item.lastIndexOf("course") + 7, item.lastIndexOf(" for"));
					coursecodes.add(coursecode);
				}
				assessmentsModel.removeAllElements();
				for (String item : coursecodes) {
					assessmentsModel.addElement(item);
				}
				assessments.setModel(assessmentsModel);
			}
		});

		assessments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String selected = (String) assessments.getSelectedItem();
				try {
					assessment = stub.getAssessment(token, studentid, selected);
					questionsModel.removeAllElements();
					for (Question item : assessment.getQuestions()) {
						questionsModel.addElement(item.getQuestionDetail());
					}
					qs.setModel(questionsModel);
				} catch (RemoteException | UnauthorizedAccess | NoMatchingAssessment e1) {
					e1.printStackTrace();
				}
			}
		});

		qs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedQuestion = qs.getSelectedIndex();
				int answer = 0;
				try {
					answer = assessment.getSelectedAnswer(selectedQuestion);
					System.out.println("Previously selected answer " + answer);
				} catch (Exception e1) {

				}
				if (selectedQuestion < 0) {
					selectedQuestion = 0;
				}
				try {
					question = assessment.getQuestion(selectedQuestion);
					answersModel.removeAllElements();
					for (String item : question.getAnswerOptions()) {
						answersModel.addElement(item);
					}
					answers.setModel(answersModel);
					if (answer >= 0) {
						answers.setSelectedIndex(answer);
					}
				} catch (InvalidQuestionNumber e1) {
					e1.printStackTrace();
				}

			}
		});

		answers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		submitQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = answers.getSelectedIndex();
				System.out.println("selected " + selected);
				try {
					assessment.selectAnswer(selectedQuestion, selected);
					int sel = assessment.getSelectedAnswer(selectedQuestion);
					info.setText("ANSWER SUBMITTED \nQuestion:" + qs.getSelectedItem() + "\nSelected Answer:"+ answers.getSelectedItem());
				} catch (InvalidQuestionNumber e1) {
					e1.printStackTrace();
				} catch (InvalidOptionNumber e1) {
					e1.printStackTrace();
				}
			}
		});

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stub.submitAssessment(token, studentid, assessment);
					info.setText("ASSESSMENT SUBMITTED: " + assessment.getInformation());
				} catch (RemoteException | UnauthorizedAccess | NoMatchingAssessment e1) {
					e1.printStackTrace();
				}
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
		contentPane.add(summary);
		contentPane.add(assessmentSummary);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(selectAssessment);
		contentPane.add(assessments);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(selectQuestion);
		contentPane.add(qs);
		contentPane.add(selectAnswer);
		contentPane.add(answers);
		contentPane.add(Box.createRigidArea(new Dimension(5, 10)));
		contentPane.add(new JLabel("Submission Info"));
		contentPane.add(info);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(submitQuestion);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		contentPane.add(submit);
		contentPane.add(Box.createRigidArea(new Dimension(5, 20)));
		frame.setVisible(true);
		frame.setSize(800, 700);
	}
}
