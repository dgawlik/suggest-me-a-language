package org.dgawlik.tree

import org.dgawlik.domain.FeatureRealization


data class TreeNode(var parent: TreeNode?, var left: TreeNode?, var right: TreeNode?, var rule: FeatureRealization?)