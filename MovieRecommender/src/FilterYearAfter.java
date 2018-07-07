
public class FilterYearAfter implements Filter {
	private int myYear;
	public FilterYearAfter(int year) {
		myYear = year;
	}
	
	@Override
	public boolean satisfies(String id) {
		return MovieDatabase.getYear(id) >= myYear;
	}

}
