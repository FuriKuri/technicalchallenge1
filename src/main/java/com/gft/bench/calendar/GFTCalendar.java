package com.gft.bench.calendar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

class GFTCalendar implements Iterable<LocalDate> {
    private LocalDate initialDate;

    GFTCalendar() {
    }

    private LocalDate getInitialDate() {

        return initialDate;
    }

    void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public Iterator<LocalDate> iterator() {
        return new GFTIterator();
    }

    private class GFTIterator implements Iterator<LocalDate> {

        LocalDate currentDay;

        void setCurrentDay(LocalDate currentDay) {

            this.currentDay = currentDay;
        }

        GFTIterator() {

            currentDay = getInitialDate();
        }

        public boolean hasNext() {

            return true;
        }

        public LocalDate next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            while (true) {
                this.setCurrentDay(currentDay.plusDays(1));
                if (currentDay.getDayOfWeek() == DayOfWeek.TUESDAY
                        || currentDay.getDayOfWeek() == DayOfWeek.FRIDAY) {
                    break;
                }
            }
            return currentDay;
        }
    }

}
