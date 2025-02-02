package io.apicurio.registry.rest.v2;

import io.apicurio.registry.rest.v2.beans.ArtifactMetaData;
import io.apicurio.registry.rest.v2.beans.ArtifactReference;
import io.apicurio.registry.rest.v2.beans.ArtifactSearchResults;
import io.apicurio.registry.rest.v2.beans.Comment;
import io.apicurio.registry.rest.v2.beans.GroupMetaData;
import io.apicurio.registry.rest.v2.beans.GroupSearchResults;
import io.apicurio.registry.rest.v2.beans.SearchedArtifact;
import io.apicurio.registry.rest.v2.beans.SearchedGroup;
import io.apicurio.registry.rest.v2.beans.SearchedVersion;
import io.apicurio.registry.rest.v2.beans.SortOrder;
import io.apicurio.registry.rest.v2.beans.VersionMetaData;
import io.apicurio.registry.rest.v2.beans.VersionSearchResults;
import io.apicurio.registry.storage.dto.ArtifactMetaDataDto;
import io.apicurio.registry.storage.dto.ArtifactReferenceDto;
import io.apicurio.registry.storage.dto.ArtifactSearchResultsDto;
import io.apicurio.registry.storage.dto.ArtifactVersionMetaDataDto;
import io.apicurio.registry.storage.dto.CommentDto;
import io.apicurio.registry.storage.dto.EditableArtifactMetaDataDto;
import io.apicurio.registry.storage.dto.GroupMetaDataDto;
import io.apicurio.registry.storage.dto.GroupSearchResultsDto;
import io.apicurio.registry.storage.dto.VersionSearchResultsDto;
import io.apicurio.registry.types.ArtifactState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class V2ApiUtil {

    private V2ApiUtil() {
    }

    /**
     * Creates a jax-rs meta-data entity from the id, type, and artifactStore meta-data.
     *
     * @param groupId
     * @param artifactId
     * @param artifactType
     * @param dto
     */
    public static ArtifactMetaData dtoToMetaData(String groupId, String artifactId,
                                                 String artifactType, ArtifactMetaDataDto dto) {
        ArtifactMetaData metaData = new ArtifactMetaData();
        metaData.setCreatedBy(dto.getOwner());
        metaData.setCreatedOn(new Date(dto.getCreatedOn()));
        metaData.setDescription(dto.getDescription());
        if (groupId != null) {
            metaData.setGroupId(groupId);
        } else {
            metaData.setGroupId(dto.getGroupId());
        }
        if (artifactId != null) {
            metaData.setId(artifactId);
        } else {
            metaData.setId(dto.getArtifactId());
        }
        metaData.setModifiedBy(dto.getModifiedBy());
        metaData.setModifiedOn(new Date(dto.getModifiedOn()));
        metaData.setName(dto.getName());
        if (artifactType != null) {
            metaData.setType(artifactType);
        } else {
            metaData.setType(dto.getType());
        }
        metaData.setState(ArtifactState.ENABLED); // TODO artifact state has gone away from the storage layer
        metaData.setLabels(toV2Labels(dto.getLabels()));
        metaData.setProperties(toV2Properties(dto.getLabels()));
        return metaData;
    }

    /**
     * Converts v3 labels into v2 properties.
     * @param v3Labels
     * @return
     */
    public static Map<String, String> toV2Properties(Map<String, String> v3Labels) {
        Map<String, String> rval = new LinkedHashMap<>();
        if (v3Labels != null) {
            v3Labels.entrySet().forEach(entry -> {
                if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                    rval.put(entry.getKey(), entry.getValue());
                }
            });
        }
        if (rval.isEmpty()) {
            return null;
        }
        return rval;
    }

    /**
     * Converts v3 labels into v2 labels.
     * @param v3Labels
     */
    public static List<String> toV2Labels(Map<String, String> v3Labels) {
        List<String> rval = new ArrayList<>();
        if (v3Labels != null) {
            v3Labels.entrySet().forEach(entry -> {
                if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                    rval.add(entry.getKey());
                }
            });
        }
        if (rval.isEmpty()) {
            return null;
        }
        return rval;
    }

    /**
     * Converts v2 labels and properties into v3 labels.
     * @param v2Labels
     * @param v2Properties
     */
    public static Map<String, String> toV3Labels(List<String> v2Labels, Map<String, String> v2Properties) {
        Map<String, String> rval = new LinkedHashMap<>();
        if (v2Labels != null) {
            v2Labels.forEach(label -> rval.put(label, null));
        }
        if (v2Properties != null) {
            rval.putAll(v2Properties);
        }
        if (rval.isEmpty()) {
            return null;
        }
        return rval;
    }

    /**
     * @param groupId
     * @param artifactId
     * @param artifactType
     * @param dto
     */
    public static ArtifactMetaData dtoToMetaData(String groupId, String artifactId, String artifactType,
                                                       ArtifactVersionMetaDataDto dto) {
        ArtifactMetaData metaData = new ArtifactMetaData();
        metaData.setCreatedBy(dto.getOwner());
        metaData.setCreatedOn(new Date(dto.getCreatedOn()));
        metaData.setDescription(dto.getDescription());
        metaData.setGroupId(groupId);
        metaData.setId(artifactId);
        metaData.setModifiedBy(dto.getOwner());
        metaData.setModifiedOn(new Date(dto.getCreatedOn()));
        metaData.setName(dto.getName());
        if (artifactType != null) {
            metaData.setType(artifactType);
        } else {
            metaData.setType(dto.getType());
        }
        metaData.setVersion(dto.getVersion());
        metaData.setGlobalId(dto.getGlobalId());
        metaData.setContentId(dto.getContentId());
        metaData.setState(ArtifactState.valueOf(dto.getState().name()));
        metaData.setLabels(toV2Labels(dto.getLabels()));
        metaData.setProperties(toV2Properties(dto.getLabels()));
        return metaData;
    }


    /**
     * Creates a jax-rs version meta-data entity from the id, type, and artifactStore meta-data.
     *
     * @param groupId
     * @param artifactId
     * @param artifactType
     * @param dto
     */
    public static VersionMetaData dtoToVersionMetaData(String groupId, String artifactId,
                                                             String artifactType, ArtifactMetaDataDto dto) {
        VersionMetaData metaData = new VersionMetaData();
        metaData.setGroupId(groupId);
        metaData.setId(artifactId);
        metaData.setCreatedBy(dto.getOwner());
        metaData.setCreatedOn(new Date(dto.getCreatedOn()));
        metaData.setDescription(dto.getDescription());
        metaData.setName(dto.getName());
        metaData.setType(artifactType);
        metaData.setState(ArtifactState.ENABLED); // This is ok because this method is only called when creating an artifact version
        metaData.setLabels(toV2Labels(dto.getLabels()));
        metaData.setProperties(toV2Properties(dto.getLabels()));
        return metaData;
    }

    /**
     * Creates a jax-rs version meta-data entity from the id, type, and artifactStore meta-data.
     *
     * @param artifactId
     * @param artifactType
     * @param dto
     */
    public static VersionMetaData dtoToVersionMetaData(String groupId, String artifactId, String artifactType,
                                                             ArtifactVersionMetaDataDto dto) {
        VersionMetaData metaData = new VersionMetaData();
        metaData.setGroupId(groupId);
        metaData.setId(artifactId);
        metaData.setCreatedBy(dto.getOwner());
        metaData.setCreatedOn(new Date(dto.getCreatedOn()));
        metaData.setDescription(dto.getDescription());
        metaData.setName(dto.getName());
        metaData.setType(artifactType);
        metaData.setVersion(dto.getVersion());
        metaData.setGlobalId(dto.getGlobalId());
        metaData.setContentId(dto.getContentId());
        metaData.setState(ArtifactState.fromValue(dto.getState().name()));
        metaData.setLabels(toV2Labels(dto.getLabels()));
        metaData.setProperties(toV2Properties(dto.getLabels()));
        return metaData;
    }

    /**
     * Sets values from the EditableArtifactMetaDataDto into the ArtifactMetaDataDto.
     *
     * @param amdd
     * @param editableArtifactMetaData
     * @return the updated ArtifactMetaDataDto object
     */
    public static ArtifactMetaDataDto setEditableMetaDataInArtifact(ArtifactMetaDataDto amdd, EditableArtifactMetaDataDto editableArtifactMetaData) {
        if (editableArtifactMetaData.getName() != null) {
            amdd.setName(editableArtifactMetaData.getName());
        }
        if (editableArtifactMetaData.getDescription() != null) {
            amdd.setDescription(editableArtifactMetaData.getDescription());
        }
        if (editableArtifactMetaData.getLabels() != null && !editableArtifactMetaData.getLabels().isEmpty()) {
            amdd.setLabels(editableArtifactMetaData.getLabels());
        }
        return amdd;
    }

    public static Comparator<ArtifactMetaDataDto> comparator(SortOrder sortOrder) {
        return (id1, id2) -> compare(sortOrder, id1, id2);
    }

    public static int compare(SortOrder sortOrder, ArtifactMetaDataDto metaDataDto1, ArtifactMetaDataDto metaDataDto2) {
        String name1 = metaDataDto1.getName();
        if (name1 == null) {
            name1 = metaDataDto1.getArtifactId();
        }
        String name2 = metaDataDto2.getName();
        if (name2 == null) {
            name2 = metaDataDto2.getArtifactId();
        }
        return sortOrder == SortOrder.desc ? name2.compareToIgnoreCase(name1) : name1.compareToIgnoreCase(name2);
    }

    public static ArtifactSearchResults dtoToSearchResults(ArtifactSearchResultsDto dto) {
        ArtifactSearchResults results = new ArtifactSearchResults();
        results.setCount((int) dto.getCount());
        results.setArtifacts(new ArrayList<>(dto.getArtifacts().size()));
        dto.getArtifacts().forEach(artifact -> {
            SearchedArtifact sa = new SearchedArtifact();
            sa.setCreatedBy(artifact.getOwner());
            sa.setCreatedOn(artifact.getCreatedOn());
            sa.setDescription(artifact.getDescription());
            sa.setId(artifact.getArtifactId());
            sa.setGroupId(artifact.getGroupId());
            sa.setModifiedBy(artifact.getModifiedBy());
            sa.setModifiedOn(artifact.getModifiedOn());
            sa.setName(artifact.getName());
            sa.setState(ArtifactState.ENABLED);
            sa.setType(artifact.getType());
            results.getArtifacts().add(sa);
        });
        return results;
    }

    public static GroupSearchResults dtoToSearchResults(GroupSearchResultsDto dto) {
        GroupSearchResults results = new GroupSearchResults();
        results.setCount((int) dto.getCount());
        results.setGroups(new ArrayList<>(dto.getGroups().size()));
        dto.getGroups().forEach(group -> {
            SearchedGroup sg = new SearchedGroup();
            sg.setCreatedBy(group.getOwner());
            sg.setCreatedOn(group.getCreatedOn());
            sg.setDescription(group.getDescription());
            sg.setId(group.getId());
            sg.setModifiedBy(group.getModifiedBy());
            sg.setModifiedOn(group.getModifiedOn());
            results.getGroups().add(sg);
        });
        return results;
    }

    public static VersionSearchResults dtoToSearchResults(VersionSearchResultsDto dto) {
        VersionSearchResults results = new VersionSearchResults();
        results.setCount((int) dto.getCount());
        results.setVersions(new ArrayList<>(dto.getVersions().size()));
        dto.getVersions().forEach(version -> {
            SearchedVersion sv = new SearchedVersion();
            sv.setCreatedBy(version.getOwner());
            sv.setCreatedOn(version.getCreatedOn());
            sv.setDescription(version.getDescription());
            sv.setGlobalId(version.getGlobalId());
            sv.setContentId(version.getContentId());
            sv.setName(version.getName());
            sv.setState(ArtifactState.fromValue(version.getState().name()));
            sv.setType(version.getType());
            sv.setVersion(version.getVersion());
            results.getVersions().add(sv);
        });
        return results;
    }

    public static ArtifactReferenceDto referenceToDto(ArtifactReference reference) {
        final ArtifactReferenceDto artifactReference = new ArtifactReferenceDto();
        artifactReference.setGroupId(reference.getGroupId());
        artifactReference.setName(reference.getName());
        artifactReference.setVersion(reference.getVersion());
        artifactReference.setArtifactId(reference.getArtifactId());
        return artifactReference;
    }

    public static ArtifactReference referenceDtoToReference(ArtifactReferenceDto reference) {
        final ArtifactReference artifactReference = new ArtifactReference();
        artifactReference.setGroupId(reference.getGroupId());
        artifactReference.setName(reference.getName());
        artifactReference.setVersion(reference.getVersion());
        artifactReference.setArtifactId(reference.getArtifactId());
        return artifactReference;
    }

    public static GroupMetaData groupDtoToGroup(GroupMetaDataDto dto) {
        GroupMetaData group = new GroupMetaData();
        group.setId(dto.getGroupId());
        group.setDescription(dto.getDescription());
        group.setCreatedBy(dto.getOwner());
        group.setModifiedBy(dto.getModifiedBy());
        group.setCreatedOn(new Date(dto.getCreatedOn()));
        group.setModifiedOn(new Date(dto.getModifiedOn()));
        group.setProperties(dto.getLabels());
        return group;
    }

    public static Comment commentDtoToComment(CommentDto dto) {
        return Comment.builder()
                .commentId(dto.getCommentId())
                .createdBy(dto.getOwner())
                .createdOn(new Date(dto.getCreatedOn()))
                .value(dto.getValue())
                .build();
    }

    public static String prettyPrintReferences(Collection<ArtifactReference> references) {
        return references.stream()
                .map(ar -> nullGroupIdToDefault(ar.getGroupId()) + ":" + ar.getArtifactId() + ":" + ar.getVersion() + "->" + ar.getName())
                .reduce((left, right) -> left + ", " + right)
                .orElse("");
    }

    public static String defaultGroupIdToNull(String groupId) {
        if ("default".equalsIgnoreCase(groupId)) {
            return null;
        }
        return groupId;
    }

    public static String nullGroupIdToDefault(String groupId) {
        return groupId != null ? groupId : "default";
    }
}
