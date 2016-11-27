package com.goods.process;

import java.util.Map;

interface ICheckRequest {
	CheckRequestResult checkRequest(BaseProcess process, Map<String, String> request, String client);
}
