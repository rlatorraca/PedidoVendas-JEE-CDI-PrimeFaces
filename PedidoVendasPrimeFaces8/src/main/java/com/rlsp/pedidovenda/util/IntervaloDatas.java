package com.rlsp.pedidovenda.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

public class IntervaloDatas {
	
	public static int getIntefvaloDeDias(Date data1, Date data2) {
		DateTime dataInicio = new DateTime(data1.getTime());
		DateTime dataFim = new DateTime(data2.getTime());
		
		Interval interval = new Interval(dataInicio, dataFim);
		Period duracao = interval.toPeriod();
		
		int intervalo = duracao.getDays();
		
		return intervalo;
	}
}