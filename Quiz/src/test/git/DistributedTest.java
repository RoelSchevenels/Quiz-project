package test.git;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DistributedTest {
	public static void main(String arg[])
	{
		ExecutorService ex = Executors.newCachedThreadPool();
		Sender j = new Sender("127.0.0.1", 1337, "John");
		Sender p = new Sender("127.0.0.1", 1337, "Paul");
		Receiver server = new Receiver();
		p.setSentence("You say goodbye and I say hello <done>");

		ex.execute(server);
		ex.execute(j);
		ex.execute(p);
		ex.shutdown();
	}
}
