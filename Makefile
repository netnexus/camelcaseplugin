SCRIPTS_DIR := scripts
LOCAL_CONFIG := $(SCRIPTS_DIR)/local.mk

-include $(LOCAL_CONFIG)

GRADLE ?= ./gradlew
GRADLE_FLAGS ?= --no-daemon --no-configuration-cache
GRADLE_ARGS ?=
GRADLE_ENV := $(if $(JAVA_HOME),JAVA_HOME="$(JAVA_HOME)")

export LOCAL_IDE_PATH MAVEN_MIRROR_URL GRADLE_PLUGIN_MIRROR_URL SKIP_INTELLIJ_AUXILIARY_TASKS GRADLE_DISTRIBUTION_URL

.PHONY: build clean package verify help

help:
	@echo "make build    Build an installable plugin ZIP"
	@echo "make verify   Validate the plugin archive structure"
	@echo "make clean    Remove build outputs"
	@echo "Optional local overrides: $(LOCAL_CONFIG)"

build package:
	$(GRADLE_ENV) $(SCRIPTS_DIR)/run-gradle.sh $(GRADLE) buildPlugin $(GRADLE_FLAGS) $(GRADLE_ARGS)

verify:
	$(GRADLE_ENV) $(SCRIPTS_DIR)/run-gradle.sh $(GRADLE) verifyPluginStructure $(GRADLE_FLAGS) $(GRADLE_ARGS)

clean:
	$(GRADLE_ENV) $(SCRIPTS_DIR)/run-gradle.sh $(GRADLE) clean $(GRADLE_FLAGS) $(GRADLE_ARGS)
