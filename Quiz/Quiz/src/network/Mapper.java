package network;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tetris.Game;

import BussinesLayer.Answer;
import BussinesLayer.Jury;
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
import Protocol.submits.AuthSubmit;
import Protocol.submits.CorrectSubmit;
import Protocol.submits.Submit;
import Protocol.submits.TetrisStartSubmit;
import Protocol.submits.TetrisSubmit;
import Util.ConnectionUtil;
import Util.DatabaseUtil;
/**
 * @author Roel
 */
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
		SubmitManager.addSubmitListener(CorrectSubmit.class,
			new SubmitListener(){
				public void handleSubmit(Submit s)
				{
					try{
						Session session = ConnectionUtil.getSession();
						Transaction t = session.beginTransaction();
						CorrectSubmit cs = (CorrectSubmit)s;
						int score = cs.getScore();
						Answer answer = DatabaseUtil.getAnswer(cs.getAnswerId());
						Jury jury = (Jury)DatabaseUtil.getUser(cs.getJuryId());
						answer.correct(jury, score);
						DatabaseUtil.setAnswerCorrected(answer);
						t.commit();
					}catch (HibernateException he) {
						
					}
				}
		});
		
		/*** Specifiek voor blokken ***/
		SubmitManager.addSubmitListener(TetrisSubmit.class,
				new SubmitListener() {
					public void handleSubmit(Submit s)
					{
						TetrisSubmit ts = (TetrisSubmit) s;
						Game game = Game.getInstance();
						if (game.isActivePlayer(ts.getConnectionId())) {
							char dir = ts.getMovement();
							switch(dir){
							case 'l':
								game.left();
								break;
							case 'd':
								game.down();
								break;
							case 'r':
								game.right();
								break;
							case 'u':
								game.rotate();
								break;
							}
						}
					}
				});
		SubmitManager.addSubmitListener(TetrisStartSubmit.class, 
				new SubmitListener(){
					public void handleSubmit(Submit s)
					{
						TetrisStartSubmit tss = (TetrisStartSubmit)s;
						Game.getInstance().start(tss.getPlayer(), tss.getPieces());
					}
				});
		SubmitManager.addSubmitListener(AuthSubmit.class,
				new SubmitListener(){
					public void handleSubmit(Submit s)
					{
						if (((AuthSubmit)s).getRole().equals("player")) {
							Game.getInstance().addConnection(((AuthSubmit)s).getConnectionId());
						}
					}
				});
		/*** Einde specifiek voor blokken ***/
	}
}
