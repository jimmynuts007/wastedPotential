package wastedPotentials;

public class projectCreator {
public static void main(String[] args) {
	
try {
	XLSReader xlsReader= new XLSReader(System.getProperty("user.dir")+"//resources//input//inputForAutoSeleniumProjectGenerator.xlsx");
xlsReader.execute();
}
catch(Exception e) {
	System.out.println(e.getMessage());
}

}}

