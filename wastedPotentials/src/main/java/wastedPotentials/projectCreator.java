package wastedPotentials;

public class projectCreator {
public static void main(String[] args) {
	
try {
	XLSReader xlsReader= new XLSReader("C:\\Users\\Abhishek kumar\\git\\wastedPotential\\wastedPotentials\\src\\main\\resources\\Input\\inputForAutoSeleniumProjectGenerator.xlsx");
xlsReader.execute();
}
catch(Exception e) {
	System.out.println(e.getMessage());
}

}}

