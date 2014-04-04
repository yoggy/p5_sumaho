package p5_sumaho;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import p5_sumaho.utils.BPSCounter;
import p5_sumaho.utils.FPSCounter;

class TCPClient extends Thread {
	String host;
	int port;

	Socket socket;
	InputStream is;
	OutputStream os;

	boolean break_flag = false;
	boolean last_publish_status = false;

	int retry_interval_time = 1000;
	int publish_interval_time = 10;

	byte[] payload = null;
	boolean update_payload = false;

	FPSCounter fps_counter = new FPSCounter("publish_fps");
	BPSCounter bps_counter = new BPSCounter("public_bps");

	public TCPClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public boolean getLastPublishStatus() {
		return last_publish_status;
	}

	public float getPublishFps() {
		return fps_counter.getFPS();
	}

	public float getPublishBps() {
		return bps_counter.getBPS();
	}

	public int getPublishIntervalTime() {
		return publish_interval_time;
	}

	public void setPublishIntrvalTime(int ms) {
		this.publish_interval_time = ms;
	}

	protected void clearStatus() {
		last_publish_status = false;
		fps_counter.clear();
		bps_counter.clear();
	}

	public void run() {
		while (!break_flag) {
			// check payload
			if (payload == null) {
				clearStatus();
				try {
					sleep(retry_interval_time);
				} catch (InterruptedException e) {
				}
				continue;
			}

			// check socket status
			if (!openSocket()) {
				clearStatus();
				try {
					sleep(retry_interval_time);
				} catch (InterruptedException e) {
				}
				continue;
			}

			// payload update check
			if (update_payload == false) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
				}
				continue;
			}

			// send packet
			try {
				// build packet data
				ByteBuffer bb = ByteBuffer.allocate(4 + 4 + payload.length)
						.order(ByteOrder.LITTLE_ENDIAN);
				bb.put("SMH1".getBytes(), 0, 4);
				bb.putInt((int) payload.length);
				bb.put(payload, 0, payload.length);

				// write packet
				os.write(bb.array(), 0, bb.capacity());
				os.flush();

				last_publish_status = true;
				update_payload = false;
				fps_counter.check();
				bps_counter.check(bb.capacity());

				bb.clear();
				bb = null;
				
			} catch (Exception e) {
				closeSocket();
				e.printStackTrace();
				clearStatus();
			}
			System.gc();
		}
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
		update_payload = true;
	}

	private boolean isOpenSocket() {
		if (socket == null || os == null || is == null)
			return false;
		return true;
	}

	private boolean openSocket() {
		if (isOpenSocket())
			return true;

		try {
			socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void closeSocket() {
		try {
			is.close();
			is = null;
		} catch (Exception e) {
		}

		try {
			os.close();
			os = null;
		} catch (Exception e) {
		}

		try {
			socket.close();
			socket = null;
		} catch (Exception e) {
		}
	}

	public String getPublishBpsStr() {
		return bps_counter.getBpsStr();
	}
}