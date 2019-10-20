package no.meikle.hockey_smash;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import no.meikle.hockey_smash.domain.Activity;

public class ChallengeDay {

	private Calendar day;
	private Calendar start;
	private Calendar end;
	private Activity activity;
	private int month;

	public ChallengeDay(Calendar newDay, Calendar aStart, Calendar anEnd, Activity anActivity) {

		this.setDay(newDay);
		this.setStart(aStart);
		this.setEnd(anEnd);
		this.setActivity(anActivity);

	}

	public int getDayNumber() {
		return day.get(Calendar.DAY_OF_MONTH);
	}

	public String getDayName() {
		return new SimpleDateFormat("EEEE").format(this.getDate());
	}

	
	public Calendar getDay() {
		return day;
	}

	public void setDay(Calendar day) {
		this.day = day;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public Date getDate() {

		return day.getTime();
	}

	public boolean getIsValid() {

		return (day.get(Calendar.MONTH) == this.month) && day.compareTo(this.getStart()) >= 0
				&& day.compareTo(this.getEnd()) <= 0;
	}

	public boolean getIsActive() {
		return day.compareTo(Calendar.getInstance()) <= 0;
	}

	public boolean getHasActivity() {
		return activity != null;
	}

	public void setActivity(Activity anActivity) {
		this.activity = anActivity;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setMonth(int aMonth) {
		this.month = aMonth;

	}
}
