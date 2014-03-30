package com.stackoverflow;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 * @see http 
 *      ://stackoverflow.com/questions/7418909/swingutilites-how-to-return-values
 *      -from-another-thread-in-java
 * 
 */
public class Question7418909 {

	public static void main(String[] args) {
		Question7418909 question = new Question7418909();
		String message = "Stop?";
		System.out.println(message);
		// blocks until input dialog returns
		String answer = question.ask(message);
		System.out.println(answer);
	}

	public Question7418909() {
	}

	public String ask(String message) {
		try {
			return new Prompt(message).prompt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class Prompt implements Callable<String> {

		private final String message;

		public Prompt(String message) {
			this.message = message;
		}

		/**
		 * This will be called from the Event Dispatch Thread a.k.a. the Swing
		 * Thread.
		 */
		@Override
		public String call() throws Exception {
			return JOptionPane.showInputDialog(message);
		}

		public String prompt() throws InterruptedException, ExecutionException {
			FutureTask<String> task = new FutureTask<>(this);
			SwingUtilities.invokeLater(task);
			return task.get();
		}

	}

}