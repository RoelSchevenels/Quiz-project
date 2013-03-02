package network;

import Protocol.RequestListener;
import Protocol.RequestManager;
import Protocol.SubmitListener;
import Protocol.SubmitManager;
import Protocol.requests.ConnectToQuizRequest;
import Protocol.requests.CreateTeamRequest;
import Protocol.requests.CreateUserRequest;
import Protocol.requests.GetTeamsRequest;
import Protocol.requests.LoginRequest;
import Protocol.requests.PictureRequest;
import Protocol.requests.Request;
import Protocol.requests.TeamLoginRequest;
import Protocol.submits.Submit;
import Protocol.submits.TetrisSubmit;
import Util.DatabaseUtil;

public class Mapper {
	protected static void mapRequests()
	{
		
		RequestManager.addRequestListener(LoginRequest.class, new RequestListener() {
					public void handleRequest(Request r)
					{
						System.out.println("handle request");
						DatabaseUtil.handleLoginRequest((LoginRequest) r);
					}
				});
			
		RequestManager.addRequestListener(CreateUserRequest.class, new RequestListener() {
			public void handleRequest(Request r)
			{
				CreateUserRequest trq = (CreateUserRequest)r;
				DatabaseUtil.handleCreateUserRequest(trq);
			}
		});
		
		RequestManager.addRequestListener(CreateTeamRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				DatabaseUtil.handleCreateTeamRequest((CreateTeamRequest) r);
				
			}
		});
		
		RequestManager.addRequestListener(GetTeamsRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				DatabaseUtil.handleGetTeamsRequest((GetTeamsRequest) r);
				
			}
		});
		
		RequestManager.addRequestListener(TeamLoginRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				DatabaseUtil.handleTeamLoginRequest((TeamLoginRequest) r);
				
			}
		});
		
		RequestManager.addRequestListener(ConnectToQuizRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				DatabaseUtil.handleConnectToQuiz((ConnectToQuizRequest) r);
				
			}
		});
		
		RequestManager.addRequestListener(PictureRequest.class, new RequestListener() {
			
			@Override
			public void handleRequest(Request r) {
				DatabaseUtil.handlePictureRequest((PictureRequest) r);				
			}
		});
	}
	
	protected static void mapSubmits()
	{
		SubmitManager.addSubmitListener(TetrisSubmit.class,
				new SubmitListener() {
					public void handleSubmit(Submit r)
					{
						TetrisSubmit ts = (TetrisSubmit) r; 
						System.out.println(ts.getMovement());
					}
				});
	}
}
