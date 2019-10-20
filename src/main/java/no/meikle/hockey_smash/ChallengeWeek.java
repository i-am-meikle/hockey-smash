package no.meikle.hockey_smash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import no.meikle.hockey_smash.domain.Activity;

public class ChallengeWeek {

	static private int[] weekDays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY };
	
	private Calendar end;
	private Calendar start;
	private int week;
	private List<ChallengeDay> challengeDays;

	public ChallengeWeek(int aWeek, Calendar aStartCalendar, Calendar anEndCalendar) {

		this.week = aWeek;
		this.start = aStartCalendar;
		this.end = anEndCalendar;

	}

	public int getWeekNumber() {

		return week;
	}

	public List<ChallengeDay> getChallengeDays() {

		return challengeDays;
	}
		
	
	public void initializeChallengeDays (Map<String, Activity> challengeActivities, SimpleDateFormat ads) {
		
		challengeDays = new ArrayList<ChallengeDay>();

		// Loop over the days of the week, starting with Monday and create a new day object for each day
				
		for (int day : weekDays) {

			Calendar newDay = Calendar.getInstance();
			newDay.setFirstDayOfWeek(Calendar.MONDAY);
			newDay.setTimeZone(TimeZone.getTimeZone("GMT"));
			newDay.setWeekDate(start.get(Calendar.YEAR), week, day);
			newDay.set(Calendar.HOUR_OF_DAY, 0);
			newDay.set(Calendar.MINUTE, 0);
			newDay.set(Calendar.SECOND, 0);
			newDay.set(Calendar.MILLISECOND, 0);

			String format = ads.format(newDay.getTime());

			ChallengeDay challengeDay = new ChallengeDay(newDay, start, end, challengeActivities.get(format));
			
			challengeDays.add(challengeDay);
		}
	
	}

	public Calendar getFirstDay() {

		return challengeDays.get(0).getDay();
	}

	public Calendar getLastDay() {
		return challengeDays.get(challengeDays.size()-1).getDay();
	}

	public void setMonth(int aMonth) {

		for (ChallengeDay cd: challengeDays) {
			cd.setMonth(aMonth);			
		}
		
	}

}
