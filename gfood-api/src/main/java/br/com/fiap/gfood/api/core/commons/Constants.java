package br.com.fiap.gfood.api.core.commons;

public final class Constants
{
	private Constants()
	{
	}

	public static final String V1_CUSTOMER = "/v1/customer";
	public static final String CREATED_AT_SORT_PARAMETER = "createdAt";
	public static final String YOUR_REQUEST_PARAMETERS_DIDNT_VALIDATE = "Your request parameters didn't validate";
	public static final String MESSAGE_ERROR_EMAIL_IS_ALREADY_USED = "Email is already used.";
	public static final String THE_EMAIL_INFORMED_IS_ALREAD_USED_IN_ANOTHER_ACCOUNT = "The email informed is used in another account.";
	public static final String MESSAGE_ERROR_CUSTOMER_NOT_FOUND = "The customer requested was not found.";
	public static final String THE_PARAMETERS_THAT_WAS_SEND_IN_REQUESTS_BODY_IS_NOT_VALID_ACCORDING_BUSINESS_RULES = "The parameters that was send in request's body is not valid according with business rules.";
	public static final String THE_EMAIL_THAT_WAS_SENDED_TO_REGISTER_NEW_CUSTOMER_IS_ALREADY_USED_TRY_ANOTHER_ONE = "The email that was sended to register new customer is aleady used. Try another one.";
	public static final String THE_CUSTOMER_REQUESTED_TO_UPDATE_WAS_NOT_FOUND_TRY_AGAIN = "The customer reuqested to updated was not found. Try again.";
	public static final String METHOD_NOT_ALLOWED = "Method requested not allowes";
	public static final String METHOD_THAT_WAS_REQUESTED_IS_NOT_ALLOWED_BY_BUSINESS_LOGIC = "Method that was requested is not allowed by business logic.";
	public static final String THE_OLD_PASSWORD_MISMATCH = "The old password informed mismatch";
	public static final String THE_OLD_PASSWORD_INFORMED_BODY_REQUISITION_MISMATCHED_DETAILED = "The old password informed on the body requisition mismatched with the real password.";
	public static final String THE_CONFIRMATION_OF_NEW_PASSWORD_MISMATCH = "The confirmation password mismatched.";
	public static final String THE_CONFIRMATION_PASSWORD_INFORMED_ON_REQUEST_BODY_MISMATCHED = "The confirmation password informed on the request body mismatched with the new password. Verify and try again.";
}
