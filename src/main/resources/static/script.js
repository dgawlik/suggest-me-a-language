 const state = {
    languages: [],
    features: [],

    filteredLanguages: [],
    treeFilteredLanguages: [],

    criteria: [],

    showBrowse: false,
    showFillCriteria: false,
    showTree: false,

    tree: null,
    alreadyAnswered: "",
    currentQuestion: "",

    init() {
        this.get()
    },

    async get() {
        let r = await fetch('/languages')
        r = await r.json()

        this.languages = r
        this.filteredLanguages = r
        this.treeFilteredLanguages = r

        let f = await fetch('/features')
        f = await f.json()

        this.features = f;
        this.criteria = [];

        let t = await fetch('/tree')
        t = await t.json()

        this.tree = t
        this.currentQuestion = this.question();
    },

    updateInput(isOn, feature, value) {
        if(isOn) {
            this.criteria = this.criteria.filter(item => item.feature.id !== feature.id)
            this.criteria =  this.criteria.concat([{value: feature.fieldType.min ? value : 1, feature}]);

            this.filteredLanguages = this.languages.filter(item => {
                return this.criteria.every( c => {
                     return item.features.some( f => c.feature.id == f.feature.id && c.value <= f.value );
                });
            });
        }
    },

    updateLanguages(isOn, feature, value) {
        if(!isOn) {
            this.criteria =  this.criteria.concat([{value: feature.fieldType.min ? value : 1, feature}]);
        }
        else {
            this.criteria = this.criteria.filter(item => item.feature.id !== feature.id)
        }
        this.filteredLanguages = this.languages.filter(item => {
            return this.criteria.every( c => {
                 return item.features.some( f => c.feature.id == f.feature.id && c.value <= f.value );
            });
        });

        return !isOn;
    },

    question()  {
        if(this.tree.rule.feature.fieldType.min) {
            return `Do you want ${this.tree.rule.feature.description} for at least ${this.tree.rule.value}?`;
        }
        else {
            return `Do you want ${this.tree.rule.feature.description}?`
        }
    },

    traverseTreeRight() {
        this.alreadyAnswered += `<li>${this.question()} : YES </li>`;
        let prevTree = this.tree;
        this.tree = this.tree.right;
        this.currentQuestion = this.question();
        this.treeFilteredLanguages = this.treeFilteredLanguages.filter(item => {
            return item.features.some( f => prevTree.rule.feature.id == f.feature.id && prevTree.rule.value <= f.value );
        });
    },

    traverseTreeLeft(tree, alreadyAnswered) {
        this.alreadyAnswered += `<li>${this.question()} : NO </li>`;
        this.tree = this.tree.left;
        this.currentQuestion = this.question();
    }
};

const featureDescription = (feature) => {
    if (feature.feature.fieldType.min) {
        return `<span>${featureMark(feature)} &nbsp; ${feature.feature.description} <progress style="display: inline-block; width: 200px; margin-top: auto; margin-bottom: auto;" value="${feature.value}" max="${feature.feature.fieldType.max}"></progress><span>`
    }
    else {
        return `${featureMark(feature)} &nbsp; ${feature.feature.description}`
    }
};

const featureMark = (feature) => {
    const id = feature.feature.id;
    if(id.startsWith("LANG")) {
        return '🟦 ';
    }
    else if(id.startsWith("TARGET")) {
        return '🟩 ';
    }
    else if(id.startsWith("SUPPORT")){
        return '🟧 '
    }
    else if(id.startsWith("PERF")){
        return '🟥 '
    }
    else return '';
};