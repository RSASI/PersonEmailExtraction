package contactextraction_phase1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public class ThreadScheduler implements Callable{


String filename;
 String docHelper;
			
			
			
			
			
			
	
	public ThreadScheduler(String docHelper,String Filename) {
				super();
				this.filename = Filename;
				this.docHelper = docHelper;
			}





	


	@Override
	public Object call() throws IOException, InterruptedException  {
                BlockIdentification.processExecution(docHelper,filename);
		return null;
	}
	
	
	

}
