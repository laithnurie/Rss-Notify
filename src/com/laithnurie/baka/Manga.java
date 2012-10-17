package com.laithnurie.baka;

public class Manga {
	
		private long id;
		private String manga;
		private String chapter;
		private String desc;

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
		
		public String getDesc() {
			return desc;
		}
		
		public void setDesc(String desc) {
			this.desc = desc;
		}	
}
