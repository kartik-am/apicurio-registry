package io.apicurio.registry.storage;

/**
 * Exception indicating that a specific markdown content was not found.
 */
public class MarkdownNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 5822213272612950365L;

    private String groupId;
    private String artifactId;

    /**
     * Default constructor.
     */
    public MarkdownNotFoundException() {
        super();
    }

    /**
     * Constructor with group ID and artifact ID.
     * @param groupId the group ID of the markdown content
     * @param artifactId the artifact ID of the markdown content
     */
    public MarkdownNotFoundException(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    /**
     * Constructor with group ID, artifact ID, and cause of the exception.
     * @param groupId the group ID of the markdown content
     * @param artifactId the artifact ID of the markdown content
     * @param cause the cause of the exception
     */
    public MarkdownNotFoundException(String groupId, String artifactId, Throwable cause) {
        super("Markdown content for artifact with ID '" + artifactId + "' in group '" + groupId + "' not found.", cause);
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    /**
     * Gets the artifact ID.
     * @return the artifact ID
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Gets the group ID.
     * @return the group ID
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Overrides the getMessage method to provide a detailed exception message.
     * @return the exception message
     */
    @Override
    public String getMessage() {
        return "No markdown content for artifact with ID '" + this.artifactId + "' in group '" + this.groupId + "' was found.";
    }
}