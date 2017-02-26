package com.popular.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.popular.common.util.DateUtils;
import com.popular.common.util.MD5Utils;

import net.sf.json.JSONObject;

@JsonInclude(Include.NON_NULL)
public class ClientUser implements Serializable {
	private static final long serialVersionUID = 4678607183602926486L;

	public ClientUser() {
		super();
	}

	public ClientUser(Integer id) {
		super();
		this.id = id;
	}

	public ClientUser(JSONObject user) {
		this.id = user.containsKey("id") ? user.getInt("id") : null;
		this.phoneNumber = user.containsKey("phoneNumber") ? user.getString("phoneNumber") : null;
		this.password = user.containsKey("password") ? MD5Utils.md5Encode(user.getString("password")) : null;
		this.code = user.containsKey("code") ? user.getString("code") : null;
		this.nickName = user.containsKey("nickName") ? user.getString("nickName") : null;
		this.age = user.containsKey("age") ? user.getInt("age") : null;
		this.career = user.containsKey("career") ? user.getString("career") : null;
		this.city = user.containsKey("city") ? user.getString("city") : null;
		this.photoUrl = user.containsKey("photoUrl") ? user.getString("photoUrl") : null;
		this.motto = user.containsKey("motto") ? user.getString("motto") : null;
		this.sexualOrientation = user.containsKey("sexualOrientation") ? user.getInt("sexualOrientation") : 1;
		this.emotion = user.containsKey("emotion") ? user.getString("emotion") : null;
		this.ambition = user.containsKey("ambition") ? user.getString("ambition") : null;
		this.individuality = user.containsKey("individuality") ? user.getString("individuality") : null;
		this.knowSkill = user.containsKey("knowSkill") ? user.getString("knowSkill") : null;
		this.footPrint = user.containsKey("footPrint") ? user.getString("footPrint") : null;
		this.writeBook = user.containsKey("writeBook") ? user.getString("writeBook") : null;
		try {
			this.createTime = user.containsKey("createTime") ? DateUtils.parse(user.getString("createTime"))
					: new Date();
			this.updateTime = user.containsKey("updateTime") ? DateUtils.parse(user.getString("updateTime"))
					: new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String md5 = this.phoneNumber + this.password + this.code + this.sexualOrientation + this.createTime;
		this.userId = user.containsKey("userId") ? user.getString("userId") : MD5Utils.md5Encode(md5);
		this.isRecommend = user.containsKey("isRecommend") ? user.getBoolean("isRecommend") : false;
		this.isVIP = user.containsKey("isVIP") ? user.getBoolean("isVIP") : false;
		this.province = user.containsKey("province") ? user.getString("province") : null;
	}

	private Integer id;
	private String userId;
	private String phoneNumber;
	private String password;
	private String code;
	private String nickName;
	private Integer age;
	private String career;
	private String city;
	private String photoUrl;
	private String motto;
	private Integer sexualOrientation;
	private String emotion;
	private String ambition;
	private String individuality;
	private String knowSkill;
	private String footPrint;
	private String writeBook;
	private Date createTime;
	private Date updateTime;
	private Boolean isRecommend;// 是否推荐
	private Integer flowerCount;// 得到花的接口
	private Boolean isVIP;// 是否VIP
	private String province;
	

	@Override
	public String toString() {
		return "id: " + this.id + ",age:" + this.age + ",userID:" + this.userId + ",phoneNumber:" + this.phoneNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public Integer getSexualOrientation() {
		return sexualOrientation;
	}

	public void setSexualOrientation(Integer sexualOrientation) {
		this.sexualOrientation = sexualOrientation;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getAmbition() {
		return ambition;
	}

	public void setAmbition(String ambition) {
		this.ambition = ambition;
	}

	public String getIndividuality() {
		return individuality;
	}

	public void setIndividuality(String individuality) {
		this.individuality = individuality;
	}

	public String getKnowSkill() {
		return knowSkill;
	}

	public void setKnowSkill(String knowSkill) {
		this.knowSkill = knowSkill;
	}

	public String getFootPrint() {
		return footPrint;
	}

	public void setFootPrint(String footPrint) {
		this.footPrint = footPrint;
	}

	public String getWriteBook() {
		return writeBook;
	}

	public void setWriteBook(String writeBook) {
		this.writeBook = writeBook;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Integer getFlowerCount() {
		return flowerCount;
	}

	public void setFlowerCount(Integer flowerCount) {
		this.flowerCount = flowerCount;
	}

	public Boolean getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(Boolean isVIP) {
		this.isVIP = isVIP;
	}

	public String getProvince() {
		return province;
	}
	

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientUser other = (ClientUser) obj;
		if(id == null) {
			if(other.id != null)
				return false;
		} else if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.getClass().getName().hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	

}
