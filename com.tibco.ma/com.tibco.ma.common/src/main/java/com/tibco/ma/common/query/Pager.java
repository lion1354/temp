package com.tibco.ma.common.query;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class Pager<T> {

	private long countrow;
	private long pagesize;
	private long countpage;
	private long nowpage = 1;
	private long startindex;
	private long endindex;
	private long maxPageIndex = 20;
	private String param;
	private List<T> data;
	private String pageStr;

	public String getPageStr() {
		return pageStr;
	}

	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	public long getCountpage() {
		return countpage;
	}

	public long getCountrow() {
		return countrow;
	}

	public List<T> getData() {
		return data;
	}

	public long getEndindex() {
		return endindex;
	}

	public long getMaxPageIndex() {
		return maxPageIndex;
	}

	public long getNowpage() {
		return nowpage;
	}

	public long getPagesize() {
		return pagesize;
	}

	public String getParam() {
		return param;
	}

	public long getStartindex() {
		return startindex;
	}

	public void processPage() {
		if (countpage >= maxPageIndex) {

			if (maxPageIndex % 2 == 0) {
				if (nowpage <= maxPageIndex / 2) {
					startindex = 1;
					endindex = maxPageIndex;
				} else {
					startindex = nowpage - maxPageIndex / 2;
					endindex = maxPageIndex / 2 + nowpage - 1;
					if (endindex > countpage) {
						long offset = endindex - countpage;
						endindex = countpage;
						startindex -= offset;
					}
				}
			} else {
				if (nowpage <= maxPageIndex / 2 + 1) {
					startindex = 1;
					endindex = maxPageIndex;
				} else {
					startindex = nowpage - maxPageIndex / 2;
					endindex = maxPageIndex / 2 + nowpage;
					if (endindex > countpage) {
						long offset = endindex - countpage;
						endindex = countpage;
						startindex -= offset;
					}
				}
			}
		} else {
			startindex = 1;
			endindex = countpage;
		}
	}

	public void setCountpage(long countpage) {
		this.countpage = countpage;
	}

	public void setCountrow(long countrow) {
		this.countrow = countrow;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public void setEndindex(long endindex) {
		this.endindex = endindex;
	}

	public void setMaxPageIndex(long maxPageIndex) {
		this.maxPageIndex = maxPageIndex;
	}

	public void setNowpage(long nowpage) {
		this.nowpage = nowpage;
	}

	public void setNowpage(String nowpage) {
		try {
			this.nowpage = Long.parseLong(nowpage);
			if (this.nowpage < 1) {
				this.nowpage = 1;
			}
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			this.nowpage = 1;
		}
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	public String showPage() {
		if (null == param || "".equals(param.trim())) {
			param = "";
		}
		processPage();
		StringBuffer sb = new StringBuffer();

		if ((nowpage - 1) < 1) {
			sb.append("<span class='disabled'> < </span>");
		} else {
			sb.append("<a href=?nowpage=").append(nowpage - 1).append("><</a>");
		}
		for (long i = startindex; i <= endindex; i++) {
			if (nowpage == i) {
				sb.append("<span class='current'>").append(i).append("</span>");
			} else {
				sb.append("<a href=?nowpage=").append(i).append(">").append(i)
						.append(" ").append("</a>");
			}
		}

		if (nowpage < countpage) {
			sb.append("<a href=?nowpage=").append(nowpage + 1).append(">></a>");
		} else {
			sb.append("<span class='disabled'> > </span>");
		}

		this.pageStr = sb.toString();
		return sb.toString();
	}

	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		Pager t = new Pager();
		t.setCountpage(66);
		t.setNowpage(34);
		t.processPage();
		System.out.println("start: " + t.getStartindex() + "," + "end: "
				+ t.getEndindex());
		StringBuffer sb = new StringBuffer();
		for (long i = t.getStartindex(); i <= t.getEndindex(); i++) {
			sb.append(i).append(",");
		}
		System.out.println(sb);
		System.out.println(t.showPage());
	}
}
