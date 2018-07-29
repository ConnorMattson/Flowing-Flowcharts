import javax.swing.SwingUtilities;

public class RunFlowingFlowcharts implements Runnable {

	public void run() {
		FlowingFlowcharts app = new FlowingFlowcharts();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new RunFlowingFlowcharts());
	}
}