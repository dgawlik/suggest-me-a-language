package org.dgawlik.tree

import com.fasterxml.jackson.annotation.JsonIgnore
import org.dgawlik.domain.FeatureRealization


class TreeNode(@JsonIgnore var parent: TreeNode?, var left: TreeNode?, var right: TreeNode?, var rule: FeatureRealization?)