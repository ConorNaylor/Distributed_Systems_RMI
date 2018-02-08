// ExamServer.java

package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import exceptions.NoMatchingAssessment;
import exceptions.UnauthorizedAccess;

public interface ExamServer extends Remote {

	// Return an access token that allows access to the server for some time period
	public long login(int studentid, String password) throws 
		UnauthorizedAccess, RemoteException;

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(long token, int studentid) throws
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(long token, int studentid, String courseCode) throws
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

	// Submit a completed assessment
	public void submitAssessment(long token, int studentid, Assessment completed) throws 
		UnauthorizedAccess, NoMatchingAssessment, RemoteException;

}

