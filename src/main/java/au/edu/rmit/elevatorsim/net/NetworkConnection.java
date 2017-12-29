package au.edu.rmit.elevatorsim.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashSet;

import org.json.JSONObject;


/**
 * Abstracts the networking code so that Controllers need only work with JSON.
 * @author Joshua Richards
 *
 */
public class NetworkConnection
{
	private Collection<Listener> listeners = new HashSet<>();
	private String host;
	private int port;

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public NetworkConnection(String host, int port) throws IOException
	{
		this.host = host;
		this.port = port;
		initSocket();
	}
	
	private void initSocket() throws IOException
	{
		socket = new Socket(host, port);
		socket.setSoTimeout(15 * 1000);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}
	
	public void addListener(Listener listener)
	{
		listeners.add(listener);
	}
	
	public boolean removeListener(Listener listener)
	{
		return listeners.remove(listener);
	}
	
	/**
	 * Synchronously listens for the next message from the server.
	 * @return the event that was received
	 * @throws IOException if there was a connection problem
	 */
	public JSONObject receiveMessage() throws IOException
	{
		synchronized (in)
		{
			String message;
			try
			{
				message = in.readUTF();
			}
			catch (SocketTimeoutException e)
			{
				// send heartbeat
				for (Listener listener : listeners)
				{
					listener.onTimeout();
				}
				// throw exception if timeout again
				message = in.readUTF();
			}
			catch (EOFException e)
			{
				// Buffer read failed (connection terminated)
				throw new IOException("Connection terminated.");
			}
			return new JSONObject(message);
		}
	}
	
	/**
	 * Synchronously transmits a message to the server
	 * @param action the message to be transmitted
	 * @throws IOException if there was a connection problem
	 */
	public void sendMessage(JSONObject action) throws IOException
	{
		synchronized (out)
		{
			out.writeUTF(action.toString());
		}
	}
	
	public void close() throws IOException
	{
		in.close();
		out.close();
		socket.close();
	}
	
	public interface Listener
	{
		public void onTimeout();
	}
}
