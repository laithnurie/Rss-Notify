package com.laithnurie.rssmanga;

public class Manga {
	
		private long id;
		private String manga;
		private String chapter;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getManga() {
			return manga;
		}

		public void setManga(String manga) {
			this.manga = manga;
		}
		
		public String getChapter() {
			return chapter;
		}
		
		public void setChapter(String chapter) {
			this.chapter = chapter;
		}

		// Will be used by the ArrayAdapter in the ListView
		@Override
		public String toString() {
			return manga;
		}
		
		
}