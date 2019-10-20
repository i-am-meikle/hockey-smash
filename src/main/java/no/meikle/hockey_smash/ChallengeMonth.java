package no.meikle.hockey_smash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChallengeMonth {

	private int month;
	private List<ChallengeWeek> weeks = new ArrayList<ChallengeWeek>();

	public ChallengeMonth(int aMonth) {

		month = aMonth;

	}

	public int getMonth() {
		return month;
	}
	
	public String getMonthString() {
		
		return new SimpleDateFormat("MMMM").format(new Calendar.Builder().setDate(1, month, 1).build().getTime());
	}

	public void addWeek(ChallengeWeek cw) {

		weeks.add(cw);
	}

	public List<ChallengeWeek> getChallengeWeeks() {
		return weeks;
	}

}
