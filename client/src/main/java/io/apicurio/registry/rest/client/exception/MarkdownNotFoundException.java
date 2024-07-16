package io.apicurio.registry.rest.client.exception;

import io.apicurio.registry.rest.v2.beans.Error;

public class MarkdownNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;

    public MarkdownNotFoundException(Error error) {
        super(error);
    }
}