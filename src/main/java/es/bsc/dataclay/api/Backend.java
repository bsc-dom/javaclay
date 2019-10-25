package es.bsc.dataclay.api;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;

public interface Backend {
	String getHostname();

	void setHostname(final String newhostname);

	String getName();

	void setName(final String newname);

	int getPort();

	void setPort(final int newport);

	Langs getLang();

	void setLang(final Langs newlang);

	BackendID getDataClayID();
}
