package com.zc.entity;

/**
 * 文章
 * @author zc
 *
 */
public class Article {
	private Integer id;//编号
	private String title;//标题
	private String content;//内容
	public Article(){
		
	}
	public Article(Integer id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}
	public Integer getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content="
				+ content + "]";
	}
}
