package com.nlogneg.matpack.operations;

import java.util.concurrent.Semaphore;

public class TicketMaster {
	
	private final Semaphore tickets;
	
	private TicketMaster() {
		tickets = new Semaphore(MatrixOperations.NUMBER_OF_PROCESSORS);
	}

	private static class TickerMasterHolder {
		private static final TicketMaster INSTANCE = new TicketMaster();
	}

	public static TicketMaster getInstance() {
		return TickerMasterHolder.INSTANCE;
	}
	
	public void GetTicket() throws InterruptedException{
		tickets.acquire();
	}
	
	public void ReplaceTicket(){
		tickets.release();
	}
}
