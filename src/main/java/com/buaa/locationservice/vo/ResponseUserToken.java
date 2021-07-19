package com.buaa.locationservice.vo;

import com.buaa.locationservice.model.User;
import lombok.Data;

@Data
public class ResponseUserToken {
	private String token;
	private User userDetail;

	public ResponseUserToken(String token, User userDetail) {
		this.token = token;
		this.userDetail = userDetail;
	}
}
