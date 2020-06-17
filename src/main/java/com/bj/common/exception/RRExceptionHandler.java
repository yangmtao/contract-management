package com.bj.common.exception;


import com.bj.common.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public void handleHttpMediaTypeNotAcceptableException(){

	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
		logger.error(e.getMessage());
		String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		if(!msg.contains("不能为空")){
			msg += "不能为空";
		}
		return R.error(500,msg);
	}
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public R handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
		logger.error(e.getMessage(), e);
		return R.error(500,"参数["+e.getParameterName() + "]不能为空");
	}
	@ExceptionHandler(BindException.class)
	public R handleBindException(BindException e){
		String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		if(!msg.contains("不能为空")){
			msg += "不能为空";
		}
		logger.error(e.getMessage(), e);
		return R.error(500,msg);
	}
	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}
