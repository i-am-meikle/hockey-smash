package no.meikle.hockey_smash.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import no.meikle.hockey_smash.domain.Message;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@InterceptorRef("jutulStack")
@Action("message")
@Namespace("/private")
@Results({ 
		@Result(name = "redirect", type="redirect", location = "${redirectUrl}")
})
@AllowedMethods({ "addMessage", "deleteMessage" })
public class MessageAction extends AbstractJutulAction implements Preparable, ModelDriven<Message> {

	private static final long serialVersionUID = 4860523392842004665L;
	private String redirectUrl;
	private Message messageObject;
	private String targetKey;
	private String messageKey;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String addMessage() throws Exception {

		this.getModel().preWrite();
		this.save(this.getModel());

		return "redirect";
	}

	public String deleteMessage() throws Exception {

		if (messageKey == null || messageKey.isEmpty()) {
			this.addActionError("The message you are trying to delete does not exist");
		} else {
			this.delete(this.getModel());
		}
		

		return "redirect";
	}
	
	@Override
	public void prepare() throws Exception {

		if(targetKey == null || (messageKey == null || messageKey.isEmpty())) {
			messageObject = new Message();
		} else {
			messageObject = this.load(Message.class, targetKey, messageKey);
		}
		
		
	}

	@Override
	public Message getModel() {
		return messageObject;
	}

	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

}
