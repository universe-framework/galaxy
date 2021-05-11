package eu.lpinto.universe.persistence.entities;

import java.util.Calendar;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public interface Repeatable {

    public Repeatable clone(final Repeatable other, final Calendar start, final Calendar end);

    public Calendar getStart();

    public void setStart(Calendar start);

    public Calendar getEnd();

    public void setEnd(Calendar end);

    public Integer getPeriod();

    public void setPeriod(Integer period);

    public String getPeriodType();

    public void setPeriodType(String periodType);

    public Calendar getPeriodUntil();

    public void setPeriodUntil(Calendar periodUntil);
}
