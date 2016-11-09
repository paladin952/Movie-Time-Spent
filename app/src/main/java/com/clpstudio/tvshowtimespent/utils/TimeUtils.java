package com.clpstudio.tvshowtimespent.utils;

import com.clpstudio.tvshowtimespent.model.DbTvShow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    /**
     * Calculate the total time spent from one show
     *
     * @param show The show
     * @return int minutes
     */
    public static int calculateTimeForOneShow(DbTvShow show) {
        int minutes = 0;
        for (int i = 0; i < Integer.parseInt(show.getNumberOfSeasons()); i++) {
            if (show.getEpisodesRunTime().size() > 0) {
                int timePerSeason = Integer.parseInt(show.getEpisodesRunTime().get(0)) * show.getSeasonsEpisodesNumber().get(i).getEpisodeCount();
                minutes += timePerSeason;
            }
        }
        return minutes;
    }

    /**
     * Calculate the total time spent
     *
     * @return A list<string> of time {day, hours, minutes}
     */
    public static List<String> calculateTimeSpent(List<DbTvShow> showList) {
        int totalMinutes = 0;
        for (DbTvShow show : showList) {
            totalMinutes += show.getMinutesTotalTime();
        }

        int seconds = totalMinutes * 60;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(day));
        result.add(String.valueOf(hours));
        result.add(String.valueOf(minute));

        return result;
    }


}
